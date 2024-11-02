package utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import entities.Account;
import entities.Transaction;
import exceptions.InvalidAccountDataException;
import exceptions.InvalidTransactionAmountsDataException;
import exceptions.InvalidTransactionDataException;
import exceptions.InvalidTransactionMatrixDataException;

import com.google.gson.GsonBuilder;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FileManager {

    private static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").setPrettyPrinting().create();

    public static List<Account> loadAccountsFromJSON(String filename) {
        List<Account> accounts = new ArrayList<>();
        try (Reader reader = new FileReader(filename)) {
            Type listType = new TypeToken<ArrayList<Account>>() {}.getType();
            List<Account> rawAccounts = gson.fromJson(reader, listType);

            if (rawAccounts != null) {
                Set<String> accountNumbers = new HashSet<>();
                for (Account account : rawAccounts) {
                    try {
                        validateAccount(account, accountNumbers);
                        accounts.add(account);
                    } catch (InvalidAccountDataException e) {
                        System.out.println("Warning: " + e.getMessage());
                    }
                }
            }
            System.out.println("Accounts loaded from " + filename);
        } catch (FileNotFoundException e) {
            System.out.println("No existing account data found in " + filename + ". Starting fresh.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return accounts;
    }
  
    private static void validateAccount(Account account, Set<String> accountNumbers) throws InvalidAccountDataException {
        if (account.getAccountNo() == null || account.getAccountNo().trim().isEmpty()) {
            throw new InvalidAccountDataException("Account number is missing or empty.");
        }
        if (account.getOwner() == null || account.getOwner().trim().isEmpty()) {
            throw new InvalidAccountDataException("Owner is missing for account number " + account.getAccountNo());
        }
        if (account.getBalance() < 0) {
            throw new InvalidAccountDataException("Balance cannot be negative for account number " + account.getAccountNo());
        }
        if (accountNumbers.contains(account.getAccountNo())) {
            throw new InvalidAccountDataException("Duplicate account number found: " + account.getAccountNo());
        }
        accountNumbers.add(account.getAccountNo());
    }

    public static void saveAccountsToJSON(List<Account> accounts, String filename) {
        try (Writer writer = new FileWriter(filename)) {
            gson.toJson(accounts, writer);
            System.out.println("Accounts saved to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Transaction> loadTransactionsFromJSON(String filename, List<Account> accountList) {
        List<Transaction> transactions = new ArrayList<>();
        try (Reader reader = new FileReader(filename)) {
            Type listType = new TypeToken<ArrayList<Transaction>>() {}.getType();
            List<Transaction> rawTransactions = gson.fromJson(reader, listType);

            if (rawTransactions != null) {
                Set<String> transactionIds = new HashSet<>();
                for (Transaction transaction : rawTransactions) {
                    try {
                        validateTransaction(transaction, transactionIds, accountList);
                        transactions.add(transaction);
                    } catch (InvalidTransactionDataException e) {
                        System.out.println("Warning: " + e.getMessage());
                    }
                }
            }
            System.out.println("Transactions loaded from " + filename);
        } catch (FileNotFoundException e) {
            System.out.println("No existing transaction data found in " + filename + ". Starting fresh.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return transactions;
    }
    
    private static void validateTransaction(Transaction transaction, Set<String> transactionIds, List<Account> accountList) throws InvalidTransactionDataException {
        if (transaction.getTransactionId() == null || transaction.getTransactionId().trim().isEmpty()) {
            throw new InvalidTransactionDataException("Transaction ID is missing or empty.");
        }
        if (transactionIds.contains(transaction.getTransactionId())) {
            throw new InvalidTransactionDataException("Duplicate transaction ID found: " + transaction.getTransactionId());
        }
        transactionIds.add(transaction.getTransactionId());

        if (transaction.getAmount() <= 0) {
            throw new InvalidTransactionDataException("Transaction amount must be positive for transaction ID " + transaction.getTransactionId());
        }
        if (transaction.getDate() == null) {
            throw new InvalidTransactionDataException("Transaction date is missing for transaction ID " + transaction.getTransactionId());
        }
        if (transaction.getType() == null) {
            throw new InvalidTransactionDataException("Transaction type is missing for transaction ID " + transaction.getTransactionId());
        }
        if (!isValidTransactionType(transaction.getType())) {
            throw new InvalidTransactionDataException("Invalid transaction type '" + transaction.getType() + "' for transaction ID " + transaction.getTransactionId());
        }
        if (transaction.getSourceAccountNo() == null || transaction.getSourceAccountNo().trim().isEmpty()) {
            throw new InvalidTransactionDataException("Source account number is missing for transaction ID " + transaction.getTransactionId());
        }
        if (!accountExists(transaction.getSourceAccountNo(), accountList)) {
            throw new InvalidTransactionDataException("Source account " + transaction.getSourceAccountNo() + " does not exist for transaction ID " + transaction.getTransactionId());
        }
        if (transaction.getType() == TransactionType.TRANSFER) {
            if (transaction.getDestinationAccountNo() == null || transaction.getDestinationAccountNo().trim().isEmpty()) {
                throw new InvalidTransactionDataException("Destination account number is missing for transfer transaction ID " + transaction.getTransactionId());
            }
            if (!accountExists(transaction.getDestinationAccountNo(), accountList)) {
                throw new InvalidTransactionDataException("Destination account " + transaction.getDestinationAccountNo() + " does not exist for transaction ID " + transaction.getTransactionId());
            }
        }
    }
    
    private static boolean isValidTransactionType(TransactionType type) {
        return type == TransactionType.DEPOSIT || type == TransactionType.WITHDRAWAL || type == TransactionType.TRANSFER;
    }
    
    private static boolean accountExists(String accountNo, List<Account> accountList) {
        for (Account account : accountList) {
            if (account.getAccountNo().equals(accountNo)) {
                return true;
            }
        }
        return false;
    }

    public static void saveTransactionsToJSON(List<Transaction> transactions, String filename) {
        try (Writer writer = new FileWriter(filename)) {
            gson.toJson(transactions, writer);
            System.out.println("Transactions saved to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void saveTransactionAmounts(double[] transactionAmounts, int transactionCount, String filename) {
        try (Writer writer = new FileWriter(filename)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            double[] amountsToSave = Arrays.copyOf(transactionAmounts, transactionCount);
            gson.toJson(amountsToSave, writer);
            System.out.println("Transaction amounts saved to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static TransactionStatistics loadTransactionAmounts(String filename) {
        TransactionStatistics stats = new TransactionStatistics(1000);
        try (Reader reader = new FileReader(filename)) {
            double[] loadedAmounts = gson.fromJson(reader, double[].class);
            if (loadedAmounts != null && loadedAmounts.length > 0) {
                validateTransactionAmounts(loadedAmounts);
                stats.setTransactionAmounts(loadedAmounts);
                stats.setTransactionCount(loadedAmounts.length);
            } else {
                System.out.println("No transaction amounts found in " + filename + ". Starting with an empty array.");
            }
        } catch (InvalidTransactionAmountsDataException e) {
            System.out.println("Warning: " + e.getMessage());
        } catch (FileNotFoundException e) {
            System.out.println("No existing transaction amounts data found in " + filename + ". Starting fresh.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stats;
    }

    private static void validateTransactionAmounts(double[] amounts) throws InvalidTransactionAmountsDataException {
        for (int i = 0; i < amounts.length; i++) {
            if (amounts[i] <= 0) {
                throw new InvalidTransactionAmountsDataException("Transaction amount at index " + i + " is non-positive.");
            }
        }
    }
    
    public static void saveTransactionMatrix(TransactionMatrix transactionMatrix, String filename) {
        try (Writer writer = new FileWriter(filename)) {
            TransactionMatrixData data = new TransactionMatrixData();
            data.setAccountIndexMap(transactionMatrix.getAccountIndexMap());
            data.setTransactionMatrix(transactionMatrix.getTransactionMatrix());
            gson.toJson(data, writer);
            System.out.println("Transaction matrix saved to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static TransactionMatrix loadTransactionMatrix(List<Account> accounts, String filename) {
        TransactionMatrix transactionMatrix = new TransactionMatrix(accounts);
        try (Reader reader = new FileReader(filename)) {
            TransactionMatrixData data = gson.fromJson(reader, TransactionMatrixData.class);
            if (data != null) {
                validateTransactionMatrixData(data, accounts);
                transactionMatrix.setAccountIndexMap(data.getAccountIndexMap());
                transactionMatrix.setTransactionMatrix(data.getTransactionMatrix());
                transactionMatrix.rebuildAccountsList(accounts);
            }
            System.out.println("Transaction matrix loaded from " + filename);
        } catch (InvalidTransactionMatrixDataException e) {
            System.out.println("Warning: " + e.getMessage());
            transactionMatrix = new TransactionMatrix(accounts);
        } catch (FileNotFoundException e) {
            System.out.println("No existing transaction matrix data found in " + filename + ". Starting fresh.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return transactionMatrix;
    }

    private static void validateTransactionMatrixData(TransactionMatrixData data, List<Account> accounts) throws InvalidTransactionMatrixDataException {
        Map<String, Integer> accountIndexMap = data.getAccountIndexMap();
        double[][] matrix = data.getTransactionMatrix();
        int accountCount = accounts.size();

        if (accountIndexMap == null || matrix == null) {
            throw new InvalidTransactionMatrixDataException("Transaction matrix data is missing.");
        }

        if (accountIndexMap.size() != accountCount || matrix.length != accountCount) {
            throw new InvalidTransactionMatrixDataException("Transaction matrix dimensions do not match the number of accounts.");
        }

        for (String accountNo : accountIndexMap.keySet()) {
            if (!accountExists(accountNo, accounts)) {
                throw new InvalidTransactionMatrixDataException("Account " + accountNo + " in transaction matrix does not exist in account list.");
            }
        }

        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i].length != accountCount) {
                throw new InvalidTransactionMatrixDataException("Transaction matrix row " + i + " has incorrect length.");
            }
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] < 0) {
                    throw new InvalidTransactionMatrixDataException("Transaction matrix contains negative value at [" + i + "][" + j + "].");
                }
            }
        }
    }


    private static class TransactionMatrixData {
        private Map<String, Integer> accountIndexMap;
        private double[][] transactionMatrix;

        public Map<String, Integer> getAccountIndexMap() {
            return accountIndexMap;
        }

        public void setAccountIndexMap(Map<String, Integer> accountIndexMap) {
            this.accountIndexMap = accountIndexMap;
        }

        public double[][] getTransactionMatrix() {
            return transactionMatrix;
        }

        public void setTransactionMatrix(double[][] transactionMatrix) {
            this.transactionMatrix = transactionMatrix;
        }
    }




}

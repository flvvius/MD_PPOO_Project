package utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import entities.Account;
import entities.Transaction;

import com.google.gson.GsonBuilder;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FileManager {

    private static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").setPrettyPrinting().create();

    public static List<Account> loadAccountsFromJSON(String filename) {
        List<Account> accounts = new ArrayList<>();
        try (Reader reader = new FileReader(filename)) {
            Type listType = new TypeToken<ArrayList<Account>>() {}.getType();
            accounts = gson.fromJson(reader, listType);
            System.out.println("Accounts loaded from " + filename);
        } catch (FileNotFoundException e) {
            System.out.println("No existing account data found in " + filename + ". Starting fresh.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    public static void saveAccountsToJSON(List<Account> accounts, String filename) {
        try (Writer writer = new FileWriter(filename)) {
            gson.toJson(accounts, writer);
            System.out.println("Accounts saved to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Transaction> loadTransactionsFromJSON(String filename) {
        List<Transaction> transactions = new ArrayList<>();
        try (Reader reader = new FileReader(filename)) {
            Type listType = new TypeToken<ArrayList<Transaction>>() {}.getType();
            transactions = gson.fromJson(reader, listType);
            if (transactions == null) {
                transactions = new ArrayList<>();
            }
            System.out.println("Transactions loaded from " + filename);
        } catch (FileNotFoundException e) {
            System.out.println("No existing transaction data found in " + filename + ". Starting fresh.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return transactions;
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
            Gson gson = new Gson();
            double[] loadedAmounts = gson.fromJson(reader, double[].class);
            if (loadedAmounts != null && loadedAmounts.length > 0) {
                stats.setTransactionAmounts(loadedAmounts);
                stats.setTransactionCount(loadedAmounts.length);
            } else {
                System.out.println("No transaction amounts found in " + filename + ". Starting with an empty array.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("No existing transaction amounts data found in " + filename + ". Starting fresh.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stats;
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
                transactionMatrix.setAccountIndexMap(data.getAccountIndexMap());
                transactionMatrix.setTransactionMatrix(data.getTransactionMatrix());
                transactionMatrix.rebuildAccountsList(accounts);
            }
            System.out.println("Transaction matrix loaded from " + filename);
        } catch (FileNotFoundException e) {
            System.out.println("No existing transaction matrix data found in " + filename + ". Starting fresh.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return transactionMatrix;
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

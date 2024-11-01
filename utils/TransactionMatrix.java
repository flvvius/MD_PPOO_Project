package utils;

import java.util.*;

import entities.Account;

public class TransactionMatrix {
    private double[][] transactionMatrix;
    private Map<String, Integer> accountIndexMap;
    private List<Account> accounts;

    public TransactionMatrix(List<Account> accounts) {
        this.accounts = new ArrayList<>(accounts);
        initializeMatrix();
    }

    private void initializeMatrix() {
        int size = accounts.size();
        transactionMatrix = new double[size][size];
        accountIndexMap = new LinkedHashMap<>();

        for (int i = 0; i < size; i++) {
            accountIndexMap.put(accounts.get(i).getAccountNo(), i);
        }
    }

    public void updateAccounts(List<Account> accounts) {
        Map<String, Integer> newAccountIndexMap = new LinkedHashMap<>();
        int index = 0;
        for (Account account : accounts) {
            newAccountIndexMap.put(account.getAccountNo(), index++);
        }

        int newSize = accounts.size();
        double[][] newTransactionMatrix = new double[newSize][newSize];

        for (Map.Entry<String, Integer> entry : accountIndexMap.entrySet()) {
            String accountNo = entry.getKey();
            Integer oldIndex = entry.getValue();
            Integer newIndex = newAccountIndexMap.get(accountNo);

            if (newIndex != null) {
                for (int i = 0; i < transactionMatrix.length; i++) {
                    if (i < newSize) {
                        newTransactionMatrix[newIndex][i] = transactionMatrix[oldIndex][i];
                        newTransactionMatrix[i][newIndex] = transactionMatrix[i][oldIndex];
                    }
                }
            }
        }

        this.transactionMatrix = newTransactionMatrix;
        this.accountIndexMap = newAccountIndexMap;
        this.accounts = new ArrayList<>(accounts);
    }

    public void recordTransaction(String sourceAccountNo, String destinationAccountNo, double amount) {
        Integer sourceIndex = accountIndexMap.get(sourceAccountNo);
        Integer destinationIndex = accountIndexMap.get(destinationAccountNo);

        if (sourceIndex == null || destinationIndex == null) {
            System.out.println("One of the accounts does not exist in the transaction matrix.");
            return;
        }

        transactionMatrix[sourceIndex][destinationIndex] += amount;
    }

    public void displayTransactionMatrix() {
        System.out.println("\nTransaction Matrix:");
        System.out.print("\t");
        for (Account acc : accounts) {
            System.out.print(acc.getAccountNo() + "\t");
        }
        System.out.println();
        for (int i = 0; i < accounts.size(); i++) {
            System.out.print(accounts.get(i).getAccountNo() + "\t");
            for (int j = 0; j < accounts.size(); j++) {
                System.out.print(transactionMatrix[i][j] + "\t");
            }
            System.out.println();
        }
    }

    public double[][] getTransactionMatrix() {
        return transactionMatrix;
    }

    public void setTransactionMatrix(double[][] transactionMatrix) {
        this.transactionMatrix = transactionMatrix;
    }

    public Map<String, Integer> getAccountIndexMap() {
        return accountIndexMap;
    }

    public void setAccountIndexMap(Map<String, Integer> accountIndexMap) {
        this.accountIndexMap = accountIndexMap;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
    
    public void rebuildAccountsList(List<Account> allAccounts) {
        this.accounts = new ArrayList<>();
        for (String accountNo : accountIndexMap.keySet()) {
            Account account = findAccountByNumber(accountNo, allAccounts);
            if (account != null) {
                accounts.add(account);
            } else {
                System.out.println("Account " + accountNo + " not found in account list.");
            }
        }
    }
    
    private Account findAccountByNumber(String accountNo, List<Account> accountList) {
        for (Account account : accountList) {
            if (account.getAccountNo().equals(accountNo)) {
                return account;
            }
        }
        return null;
    }


}

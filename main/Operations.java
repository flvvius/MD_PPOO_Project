package main;

import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import entities.Account;
import entities.Transaction;
import exceptions.InsufficientFundsException;
import exceptions.InvalidAmountException;
import utils.TransactionType;

public class Operations {
	

	private static String generateTransactionId() {
	    return UUID.randomUUID().toString();
	}
	
	static void displayMenu() {
        System.out.println("\n=== E-Banking System ===");
        System.out.println("1. Create Account");
        System.out.println("2. View Accounts");
        System.out.println("3. Deposit Funds");
        System.out.println("4. Withdraw Funds");
        System.out.println("5. Transfer Funds to Another Account");
        System.out.println("10. Exit");
        System.out.print("Please select an option: ");
    }
	
	static int getUserChoice(Scanner scanner) {
        int choice = -1;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number corresponding to the menu options.");
        }
        return choice;
    }
	
	static void createAccount(List<Account> accountList, Scanner scanner) {
        System.out.println("\n--- Create New Account ---");
        try {
            System.out.print("Enter Account Number: ");
            String accountNo = scanner.nextLine().trim();
            if (accountExists(accountNo, accountList)) {
                System.out.println("An account with this number already exists.");
                return;
            }

            System.out.print("Enter Owner Name: ");
            String owner = scanner.nextLine().trim();

            System.out.print("Enter Initial Balance: ");
            double balance = Double.parseDouble(scanner.nextLine());

            Account newAccount = new Account(accountNo, owner, balance);
            accountList.add(newAccount);
            System.out.println("Account created successfully!");

        } catch (NumberFormatException e) {
            System.out.println("Invalid input for balance. Please enter a valid number.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error creating account: " + e.getMessage());
        }
    }
	
	private static boolean accountExists(String accountNo, List<Account> accountList) {
        for (Account account : accountList) {
            if (account.getAccountNo().equals(accountNo)) {
                return true;
            }
        }
        return false;
    }
	
	static void viewAccounts(List<Account> accountList) {
	        System.out.println("\n--- List of Accounts ---");
	        if (accountList.isEmpty()) {
	            System.out.println("No accounts found.");
	            return;
	        }
	        for (Account account : accountList) {
	            System.out.println(account);
	        }
	 }
	 
	private static Account findAccountByNumber(String accountNo, List<Account> accountList) {
		    for (Account account : accountList) {
		        if (account.getAccountNo().equals(accountNo)) {
		            return account;
		        }
		    }
		    return null;
		}
	 
	static void depositFunds(List<Account> accountList, List<Transaction> transactionList, Scanner scanner) {
		    System.out.println("\n--- Deposit Funds ---");
		    try {
		        System.out.print("Enter Account Number: ");
		        String accountNo = scanner.nextLine().trim();

		        Account account = findAccountByNumber(accountNo, accountList);
		        if (account == null) {
		            System.out.println("Account not found.");
		            return;
		        }

		        System.out.print("Enter Amount to Deposit: ");
		        double amount = Double.parseDouble(scanner.nextLine());

		        account.deposit(amount);

		        Transaction transaction = new Transaction(
		            generateTransactionId(),
		            amount,
		            new Date(),
		            TransactionType.DEPOSIT,
		            accountNo
		        );
		        transactionList.add(transaction);

		        System.out.println("Deposit successful.");
		    } catch (NumberFormatException e) {
		        System.out.println("Invalid amount. Please enter a valid number.");
		    } catch (InvalidAmountException e) {
		        System.out.println("Error: " + e.getMessage());
		    }
	 }
	 
	static void withdrawFunds(List<Account> accountList, List<Transaction> transactionList, Scanner scanner) {
		    System.out.println("\n--- Withdraw Funds ---");
		    try {
		        System.out.print("Enter Account Number: ");
		        String accountNo = scanner.nextLine().trim();

		        Account account = findAccountByNumber(accountNo, accountList);
		        if (account == null) {
		            System.out.println("Account not found.");
		            return;
		        }

		        System.out.print("Enter Amount to Withdraw: ");
		        double amount = Double.parseDouble(scanner.nextLine());

		        account.withdraw(amount);

		        Transaction transaction = new Transaction(
		            generateTransactionId(),
		            amount,
		            new Date(),
		            TransactionType.WITHDRAWAL,
		            accountNo
		        );
		        transactionList.add(transaction);

		        System.out.println("Withdrawal successful.");
		    } catch (NumberFormatException e) {
		        System.out.println("Invalid amount. Please enter a valid number.");
		    } catch (InvalidAmountException | InsufficientFundsException e) {
		        System.out.println("Error: " + e.getMessage());
		    }
	 }
	 
	static void transferFunds(List<Account> accountList, List<Transaction> transactionList, Scanner scanner) {
		    System.out.println("\n--- Transfer Funds ---");
		    try {
		        System.out.print("Enter Source Account Number: ");
		        String sourceAccountNo = scanner.nextLine().trim();

		        Account sourceAccount = findAccountByNumber(sourceAccountNo, accountList);
		        if (sourceAccount == null) {
		            System.out.println("Source account not found.");
		            return;
		        }

		        System.out.print("Enter Destination Account Number: ");
		        String destinationAccountNo = scanner.nextLine().trim();

		        Account destinationAccount = findAccountByNumber(destinationAccountNo, accountList);
		        if (destinationAccount == null) {
		            System.out.println("Destination account not found.");
		            return;
		        }
		        
		        if (sourceAccountNo.equals(destinationAccountNo)) {
		            System.out.println("Source and destination accounts must be different.");
		            return;
		        }

		        System.out.print("Enter Amount to Transfer: ");
		        double amount = Double.parseDouble(scanner.nextLine());

		        sourceAccount.withdraw(amount);
		        destinationAccount.deposit(amount);

		        Transaction transaction = new Transaction(
		            generateTransactionId(),
		            amount,
		            new Date(),
		            TransactionType.TRANSFER,
		            sourceAccountNo,
		            destinationAccountNo
		        );
		        transactionList.add(transaction);

		        System.out.println("Transfer successful.");
		    } catch (NumberFormatException e) {
		        System.out.println("Invalid amount. Please enter a valid number.");
		    } catch (InvalidAmountException | InsufficientFundsException e) {
		        System.out.println("Error: " + e.getMessage());
		    }
	 }




}

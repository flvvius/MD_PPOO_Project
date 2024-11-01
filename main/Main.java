package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import entities.Account;
import utils.FileManager;

public class Main {
    private static List<Account> accountList = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        accountList = FileManager.loadAccountsFromJSON("accounts.json");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            FileManager.saveAccountsToJSON(accountList, "accounts.json");
            System.out.println("Data saved successfully.");
        }));

        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getUserChoice();
            switch (choice) {
                case 1:
                    createAccount();
                    break;
                case 2:
                    viewAccounts();
                    break;
                case 3:
                    System.out.println("Feature not implemented yet.");
                    break;
                case 4:
                    running = false;
                    System.out.println("Exiting application...");
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        }

        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\n=== E-Banking System ===");
        System.out.println("1. Create Account");
        System.out.println("2. View Accounts");
        System.out.println("3. Other Features");
        System.out.println("4. Exit");
        System.out.print("Please select an option: ");
    }

    private static int getUserChoice() {
        int choice = -1;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {

        }
        return choice;
    }

    private static void createAccount() {
        System.out.println("\n--- Create New Account ---");
        try {
            System.out.print("Enter Account Number: ");
            String accountNo = scanner.nextLine().trim();
            if (accountExists(accountNo)) {
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

    private static boolean accountExists(String accountNo) {
        for (Account account : accountList) {
            if (account.getAccountNo().equals(accountNo)) {
                return true;
            }
        }
        return false;
    }

    private static void viewAccounts() {
        System.out.println("\n--- List of Accounts ---");
        if (accountList.isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }
        for (Account account : accountList) {
            System.out.println(account);
        }
    }
}

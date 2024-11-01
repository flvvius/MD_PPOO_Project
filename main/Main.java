package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import entities.Account;
import entities.Transaction;
import utils.FileManager;

public class Main {
    private static List<Account> accountList = new ArrayList<>();
    private static List<Transaction> transactionList = new ArrayList<Transaction>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        accountList = FileManager.loadAccountsFromJSON("accounts.json");
        transactionList = FileManager.loadTransactionsFromJSON("transactions.json");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            FileManager.saveAccountsToJSON(accountList, "accounts.json");
            FileManager.saveTransactionsToJSON(transactionList, "transactions.json");
            System.out.println("Data saved successfully.");
        }));

        boolean running = true;
        while (running) {
            Operations.displayMenu();
            int choice = Operations.getUserChoice(scanner);
            switch (choice) {
                case 1:
                    Operations.createAccount(accountList, scanner);
                    break;
                case 2:
                    Operations.viewAccounts(accountList);
                    break;
                case 3:
                    Operations.depositFunds(accountList, transactionList, scanner);
                    break;
                case 4:
                	Operations.withdrawFunds(accountList, transactionList, scanner);
                	break;
                case 5:
                	Operations.transferFunds(accountList, transactionList, scanner);
                	break;
                case 6:
                	break;
                case 7:
                	break;
                case 8:
                	break;
                case 9:
                	// generare statistici si rapoarte
                	break;
                case 10:
                    running = false;
                    System.out.println("Exiting application...");
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        }


        scanner.close();
    }
}

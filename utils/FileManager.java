package utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import entities.Account;
import entities.Transaction;

import com.google.gson.GsonBuilder;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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

}

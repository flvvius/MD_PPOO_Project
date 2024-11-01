package entities;

import java.io.Serializable;

import exceptions.InsufficientFundsException;
import exceptions.InvalidAmountException;

public class Account implements Comparable<Account>, Serializable {
    private static final long serialVersionUID = 1L;
   	private String accountNo;
    private String owner;
    private double balance;
    
    public Account(String accountNo, String owner, double balance) {
        this.accountNo = accountNo;
        this.owner = owner;
        this.balance = balance;
    }
    
    public String getAccountNo() {
        return accountNo;
    }

    public String getOwner() {
        return owner;
    }

    public double getBalance() {
        return balance;
    }

    public void setAccountNo(String accountNo) {
        if (accountNo == null || accountNo.isEmpty()) {
            throw new IllegalArgumentException("Account number cannot be null or empty.");
        }
        this.accountNo = accountNo;
    }

    public void setOwner(String owner) {
        if (owner == null || owner.isEmpty()) {
            throw new IllegalArgumentException("Owner cannot be null or empty.");
        }
        this.owner = owner;
    }

    public void setBalance(double balance) {
        if (balance < 0) {
            throw new IllegalArgumentException("Balance cannot be negative.");
        }
        this.balance = balance;
    }

    public void deposit(double amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be positive.");
        }
        balance += amount;
    }
    
    public void withdraw(double amount) throws InvalidAmountException, InsufficientFundsException {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be positive.");
        }
        if (amount > balance) {
            throw new InsufficientFundsException("Insufficient funds for withdrawal.");
        }
        balance -= amount;
    }
    
    public void viewDetails() {
        System.out.println("Account [accountNo=" + accountNo + ", owner=" + owner + ", balance=" + balance + "]");
    }
    
    @Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Account [accountNo=");
		builder.append(accountNo);
		builder.append(", owner=");
		builder.append(owner);
		builder.append(", balance=");
		builder.append(balance);
		builder.append("]");
		return builder.toString();
	}

	@Override
    public int compareTo(Account other) {
        return this.accountNo.compareTo(other.accountNo);
    }
    
    public void transferTo(Account destinationAccount, double amount) throws InvalidAmountException, InsufficientFundsException {
        this.withdraw(amount);
        destinationAccount.deposit(amount);
    }
    
    public void applyInterest(double interestRate) {
        if (interestRate < 0) {
            throw new IllegalArgumentException("Interest rate cannot be negative.");
        }
        balance += balance * interestRate;
    }


}

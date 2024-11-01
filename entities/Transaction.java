package entities;

import java.util.Date;

import utils.TransactionType;

public class Transaction {
    private String transactionId;
    private double amount;
    private Date date;
    private TransactionType type;
    private String sourceAccountNo;
    private String destinationAccountNo;

    public Transaction() {}

    public Transaction(String transactionId, double amount, Date date, TransactionType type, String accountNo) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.date = date;
        this.type = type;
        if (type == TransactionType.DEPOSIT || type == TransactionType.WITHDRAWAL) {
            this.sourceAccountNo = accountNo;
        }
    }

    public Transaction(String transactionId, double amount, Date date, TransactionType type, String sourceAccountNo, String destinationAccountNo) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.date = date;
        this.type = type;
        if (type == TransactionType.TRANSFER) {
            this.sourceAccountNo = sourceAccountNo;
            this.destinationAccountNo = destinationAccountNo;
        }
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public TransactionType getType() {
        return type;
    }
    
    public void setType(TransactionType type) {
        this.type = type;
    }

    public String getSourceAccountNo() {
        return sourceAccountNo;
    }

    public void setSourceAccountNo(String sourceAccountNo) {
        this.sourceAccountNo = sourceAccountNo;
    }

    public String getDestinationAccountNo() {
        return destinationAccountNo;
    }

    public void setDestinationAccountNo(String destinationAccountNo) {
        this.destinationAccountNo = destinationAccountNo;
    }

    @Override
    public String toString() {
        return "Transaction [transactionId=" + transactionId + ", amount=" + amount + ", date=" + date + ", type=" + type
                + ", sourceAccountNo=" + sourceAccountNo + ", destinationAccountNo=" + destinationAccountNo + "]";
    }
}

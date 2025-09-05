package com.banklink.model;

import java.util.List;

public class BankAccount {
    private String accountId;
    private String type;
    private double balance;
    private List<Transaction> transactions;

    // Constructors
    public BankAccount() {
    }

    public BankAccount(String accountId, String type, double balance) {
        this.accountId = accountId;
        this.type = type;
        this.balance = balance;
    }

    // Getters and Setters
    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
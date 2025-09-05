package com.banklink.model;

import java.util.List;

public class User {
    private String id;
    private String username;
    private String password;
    private List<BankAccount> accounts;

    // Constructors
    public User() {
    }

    public User(String id, String username, String password, List<BankAccount> accounts) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.accounts = accounts;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<BankAccount> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<BankAccount> accounts) {
        this.accounts = accounts;
    }
}
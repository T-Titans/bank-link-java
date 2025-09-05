package com.banklink.service;

import com.banklink.model.BankAccount;
import com.banklink.model.Transaction;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AccountService {

    // Simulating a database with a Map. In a real app, you'd use a repository.
    private Map<String, BankAccount> accountDatabase = new HashMap<>();

    public AccountService() {
        // Hardcoded initial accounts to match your frontend's starting state
        BankAccount cheque = new BankAccount("ACC001", "Cheque", 1000.00);
        BankAccount savings = new BankAccount("SAV001", "Savings", 1000.00);
        accountDatabase.put("ACC001", cheque);
        accountDatabase.put("SAV001", savings);
    }

    public BankAccount getAccountById(String id) {
        return accountDatabase.get(id);
    }

    public void deposit(String accountId, double amount) {
        BankAccount account = getAccountById(accountId);
        if (account != null && amount > 0) {
            account.setBalance(account.getBalance() + amount);
            account.addTransaction(new Transaction("Deposit", amount));
        }
    }

    public boolean withdraw(String accountId, double amount) {
        BankAccount account = getAccountById(accountId);
        if (account != null && amount > 0 && account.getBalance() >= amount) {
            account.setBalance(account.getBalance() - amount);
            account.addTransaction(new Transaction("Withdrawal", amount));
            return true;
        }
        return false;
    }

    // Add transfer logic here...
}
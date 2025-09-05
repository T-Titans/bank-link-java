package com.banklink.service;

import com.banklink.model.BankAccount;
import com.banklink.model.Transaction;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@Service
public class AccountService {

    private Map<String, BankAccount> accountDatabase = new HashMap<>();

    public AccountService() {
        List<Transaction> chequeTransactions = new ArrayList<>();
        chequeTransactions.add(new Transaction("Opening Balance", 1000.00));
        BankAccount cheque = new BankAccount("ACC001", "Cheque", 1000.00);
        cheque.setTransactions(chequeTransactions);

        List<Transaction> savingsTransactions = new ArrayList<>();
        savingsTransactions.add(new Transaction("Opening Balance", 1000.00));
        BankAccount savings = new BankAccount("SAV001", "Savings", 1000.00);
        savings.setTransactions(savingsTransactions);

        accountDatabase.put("ACC001", cheque);
        accountDatabase.put("SAV001", savings);
    }

    public BankAccount getAccountById(String id) {
        return accountDatabase.get(id);
    }

    public Map<String, BankAccount> getAllAccounts() {
        return accountDatabase;
    }

    public void deposit(String accountId, double amount) {
        BankAccount account = getAccountById(accountId);
        if (account != null && amount > 0) {
            account.setBalance(account.getBalance() + amount);
            account.getTransactions().add(new Transaction("Deposit", amount));
        }
    }

    public boolean withdraw(String accountId, double amount) {
        BankAccount account = getAccountById(accountId);
        if (account != null && amount > 0 && account.getBalance() >= amount) {
            account.setBalance(account.getBalance() - amount);
            account.getTransactions().add(new Transaction("Withdrawal", amount));
            return true;
        }
        return false;
    }

    // New method for transfer logic
    public boolean transfer(String fromAccountId, String toAccountId, double amount) {
        // Check if accounts are valid and amount is positive
        if (fromAccountId.equals(toAccountId) || amount <= 0) {
            return false;
        }

        BankAccount fromAccount = getAccountById(fromAccountId);
        BankAccount toAccount = getAccountById(toAccountId);

        if (fromAccount == null || toAccount == null) {
            return false;
        }

        // Check for sufficient funds
        if (fromAccount.getBalance() < amount) {
            return false;
        }

        // Perform the withdrawal and deposit
        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);

        // Record a transaction for both accounts
        fromAccount.getTransactions().add(new Transaction("Transfer to " + toAccountId, amount));
        toAccount.getTransactions().add(new Transaction("Transfer from " + fromAccountId, amount));

        return true;
    }
}
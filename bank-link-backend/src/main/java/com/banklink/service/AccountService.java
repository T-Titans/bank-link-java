package com.banklink.service;

import com.banklink.model.BankAccount;
import com.banklink.model.Transaction;
import com.banklink.repository.BankAccountRepository;
import com.banklink.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

<<<<<<< HEAD
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
=======
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
>>>>>>> 0e97197142ef6d663d3926191b2578f0b5bf1739

@Service
@Transactional
public class AccountService {

<<<<<<< HEAD
    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * Get account by ID
     */
    public Optional<BankAccount> getAccountById(String accountId) {
        return bankAccountRepository.findById(accountId);
=======
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
>>>>>>> 0e97197142ef6d663d3926191b2578f0b5bf1739
    }

    /**
     * Get all accounts
     */
    public List<BankAccount> getAllAccounts() {
        return bankAccountRepository.findAll();
    }

<<<<<<< HEAD
    /**
     * Create a new bank account
     */
    public BankAccount createAccount(String accountId, String accountType, Double initialBalance) {
        if (bankAccountRepository.existsById(accountId)) {
            throw new IllegalArgumentException("Account with ID " + accountId + " already exists");
        }

        BankAccount account = new BankAccount();
        account.setAccountId(accountId);
        account.setAccountType(accountType);
        account.setBalance(initialBalance != null ? initialBalance : 0.0);
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());

        return bankAccountRepository.save(account);
    }

    /**
     * Deposit money to account
     */
    public BankAccount deposit(String accountId, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }

        Optional<BankAccount> accountOpt = getAccountById(accountId);
        if (accountOpt.isEmpty()) {
            throw new IllegalArgumentException("Account not found: " + accountId);
        }

        BankAccount account = accountOpt.get();
        account.setBalance(account.getBalance() + amount);
        account.setUpdatedAt(LocalDateTime.now());

        // Create transaction record
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setTransactionType("DEPOSIT");
        transaction.setAmount(amount);
        transaction.setDescription("Deposit to account " + accountId);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setBalanceAfter(account.getBalance());

        transactionRepository.save(transaction);
        return bankAccountRepository.save(account);
    }

    /**
     * Withdraw money from account
     */
    public BankAccount withdraw(String accountId, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }

        Optional<BankAccount> accountOpt = getAccountById(accountId);
        if (accountOpt.isEmpty()) {
            throw new IllegalArgumentException("Account not found: " + accountId);
        }

        BankAccount account = accountOpt.get();
        if (account.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient funds. Available balance: " + account.getBalance());
        }

        account.setBalance(account.getBalance() - amount);
        account.setUpdatedAt(LocalDateTime.now());

        // Create transaction record
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setTransactionType("WITHDRAWAL");
        transaction.setAmount(amount);
        transaction.setDescription("Withdrawal from account " + accountId);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setBalanceAfter(account.getBalance());

        transactionRepository.save(transaction);
        return bankAccountRepository.save(account);
    }

    /**
     * Transfer money between accounts
     */
    public void transfer(String fromAccountId, String toAccountId, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }

        if (fromAccountId.equals(toAccountId)) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }

        Optional<BankAccount> fromAccountOpt = getAccountById(fromAccountId);
        Optional<BankAccount> toAccountOpt = getAccountById(toAccountId);

        if (fromAccountOpt.isEmpty()) {
            throw new IllegalArgumentException("Source account not found: " + fromAccountId);
        }
        if (toAccountOpt.isEmpty()) {
            throw new IllegalArgumentException("Destination account not found: " + toAccountId);
        }

        BankAccount fromAccount = fromAccountOpt.get();
        BankAccount toAccount = toAccountOpt.get();

        if (fromAccount.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient funds in source account. Available balance: " + fromAccount.getBalance());
        }

        // Debit from source account
        fromAccount.setBalance(fromAccount.getBalance() - amount);
        fromAccount.setUpdatedAt(LocalDateTime.now());

        // Credit to destination account
        toAccount.setBalance(toAccount.getBalance() + amount);
        toAccount.setUpdatedAt(LocalDateTime.now());

        // Create transaction records
        Transaction debitTransaction = new Transaction();
        debitTransaction.setAccount(fromAccount);
        debitTransaction.setTransactionType("TRANSFER_OUT");
        debitTransaction.setAmount(amount);
        debitTransaction.setDescription("Transfer to account " + toAccountId);
        debitTransaction.setTransactionDate(LocalDateTime.now());
        debitTransaction.setBalanceAfter(fromAccount.getBalance());

        Transaction creditTransaction = new Transaction();
        creditTransaction.setAccount(toAccount);
        creditTransaction.setTransactionType("TRANSFER_IN");
        creditTransaction.setAmount(amount);
        creditTransaction.setDescription("Transfer from account " + fromAccountId);
        creditTransaction.setTransactionDate(LocalDateTime.now());
        creditTransaction.setBalanceAfter(toAccount.getBalance());

        // Save all changes
        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);
        bankAccountRepository.save(fromAccount);
        bankAccountRepository.save(toAccount);
    }

    /**
     * Get transaction history for an account
     */
    public List<Transaction> getTransactionHistory(String accountId) {
        Optional<BankAccount> accountOpt = getAccountById(accountId);
        if (accountOpt.isEmpty()) {
            throw new IllegalArgumentException("Account not found: " + accountId);
        }
        return transactionRepository.findByAccountIdOrderByTransactionDateDesc(accountOpt.get().getAccountId());
    }

    /**
     * Get account balance
     */
    public Double getAccountBalance(String accountId) {
        Optional<BankAccount> accountOpt = getAccountById(accountId);
        if (accountOpt.isEmpty()) {
            throw new IllegalArgumentException("Account not found: " + accountId);
        }
        return accountOpt.get().getBalance();
    }

    /**
     * Initialize default accounts (for development/testing)
     */
    public void initializeDefaultAccounts() {
        if (bankAccountRepository.count() == 0) {
            createAccount("ACC001", "Cheque", 1000.00);
            createAccount("SAV001", "Savings", 1000.00);
        }
    }

    /**
     * Delete account (soft delete or hard delete based on requirements)
     */
    public void deleteAccount(String accountId) {
        Optional<BankAccount> accountOpt = getAccountById(accountId);
        if (accountOpt.isEmpty()) {
            throw new IllegalArgumentException("Account not found: " + accountId);
=======
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
>>>>>>> 0e97197142ef6d663d3926191b2578f0b5bf1739
        }
        
        BankAccount account = accountOpt.get();
        if (account.getBalance() != 0) {
            throw new IllegalArgumentException("Cannot delete account with non-zero balance: " + account.getBalance());
        }
        
        bankAccountRepository.delete(account);
    }
<<<<<<< HEAD
=======

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
>>>>>>> 0e97197142ef6d663d3926191b2578f0b5bf1739
}
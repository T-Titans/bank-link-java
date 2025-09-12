package com.banklink.service;

import com.banklink.model.BankAccount;
import com.banklink.model.Transaction;
import com.banklink.repository.BankAccountRepository;
import com.banklink.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * Get account by ID
     */
    public Optional<BankAccount> getAccountById(String accountId) {
        return bankAccountRepository.findById(accountId);
    }

    /**
     * Get all accounts
     */
    public List<BankAccount> getAllAccounts() {
        return bankAccountRepository.findAll();
    }

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
        }
        
        BankAccount account = accountOpt.get();
        if (account.getBalance() != 0) {
            throw new IllegalArgumentException("Cannot delete account with non-zero balance: " + account.getBalance());
        }
        
        bankAccountRepository.delete(account);
    }
}
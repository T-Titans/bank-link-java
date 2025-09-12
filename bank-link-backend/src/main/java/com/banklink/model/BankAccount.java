package com.banklink.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bank_accounts")
public class BankAccount {

    @Id
    @Column(name = "account_id")
    private String accountId; // Using String ID as per your AccountService

    @Column(name = "account_type", nullable = false)
    @NotBlank(message = "Account type is required")
    private String accountType; // e.g., "Cheque", "Savings", "Credit"

    @Column(name = "balance", nullable = false)
    @NotNull(message = "Balance is required")
    @PositiveOrZero(message = "Balance cannot be negative")
    private Double balance = 0.0;

    @Column(name = "account_name")
    private String accountName;

    @Column(name = "account_status")
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus = AccountStatus.ACTIVE;

    @Column(name = "currency")
    private String currency = "ZAR"; // South African Rand

    @Column(name = "interest_rate")
    private Double interestRate = 0.0;

    @Column(name = "overdraft_limit")
    private Double overdraftLimit = 0.0;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "last_transaction_date")
    private LocalDateTime lastTransactionDate;

    // Many bank accounts belong to one user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference // Prevents circular reference when serializing
    private User user;

    // One account has many transactions
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference // Manages the serialization of transactions
    private List<Transaction> transactions = new ArrayList<>();

    // Constructors
    public BankAccount() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.accountStatus = AccountStatus.ACTIVE;
        this.balance = 0.0;
        this.currency = "ZAR";
    }

    public BankAccount(String accountId, String accountType) {
        this();
        this.accountId = accountId;
        this.accountType = accountType;
    }

    public BankAccount(String accountId, String accountType, Double initialBalance) {
        this(accountId, accountType);
        this.balance = initialBalance != null ? initialBalance : 0.0;
    }

    // Getters and Setters
    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
        this.lastTransactionDate = LocalDateTime.now();
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public Double getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(Double overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getLastTransactionDate() {
        return lastTransactionDate;
    }

    public void setLastTransactionDate(LocalDateTime lastTransactionDate) {
        this.lastTransactionDate = lastTransactionDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    // Utility Methods
    public String getFormattedBalance() {
        return String.format("%.2f", balance);
    }

    public boolean isActive() {
        return accountStatus == AccountStatus.ACTIVE;
    }

    public boolean canWithdraw(Double amount) {
        return (balance + overdraftLimit) >= amount;
    }

    public Double getAvailableBalance() {
        return balance + overdraftLimit;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        transaction.setAccount(this);
        this.lastTransactionDate = LocalDateTime.now();
    }

    public boolean isSavingsAccount() {
        return "SAVINGS".equalsIgnoreCase(accountType);
    }

    public boolean isChequeAccount() {
        return "CHEQUE".equalsIgnoreCase(accountType);
    }

    // Lifecycle callbacks
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (this.createdAt == null) {
            this.createdAt = now;
        }
        if (this.updatedAt == null) {
            this.updatedAt = now;
        }
        if (this.balance == null) {
            this.balance = 0.0;
        }
    }

    // Enums
    public enum AccountStatus {
        ACTIVE,
        INACTIVE,
        SUSPENDED,
        CLOSED,
        FROZEN
    }

    public enum AccountType {
        CHEQUE("Cheque"),
        SAVINGS("Savings"),
        CREDIT("Credit"),
        INVESTMENT("Investment");

        private final String displayName;

        AccountType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "accountId='" + accountId + '\'' +
                ", accountType='" + accountType + '\'' +
                ", balance=" + balance +
                ", accountStatus=" + accountStatus +
                ", currency='" + currency + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccount that = (BankAccount) o;
        return accountId != null && accountId.equals(that.accountId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
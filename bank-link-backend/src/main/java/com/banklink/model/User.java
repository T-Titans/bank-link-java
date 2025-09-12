package com.banklink.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private String name;        // Maps to "First Name" from frontend
    
    @Column(nullable = false)
    private String surname;     // Maps to "Last Name" from frontend
    
    @Column(unique = true, nullable = false)
    private String idNumber;
    
    @Column(nullable = false)
    private String fullName;
    
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus = AccountStatus.ACTIVE;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Add relationship to bank accounts
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<BankAccount> bankAccounts = new ArrayList<>();

    public enum AccountStatus {
        ACTIVE, INACTIVE, SUSPENDED
    }

    // Constructors
    public User() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public User(String email, String password, String name, String surname, String idNumber) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.idNumber = idNumber;
        this.fullName = name + " " + surname;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.accountStatus = AccountStatus.ACTIVE;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { 
        this.name = name;
        updateFullName();
    }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { 
        this.surname = surname;
        updateFullName();
    }

    public String getIdNumber() { return idNumber; }
    public void setIdNumber(String idNumber) { this.idNumber = idNumber; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public AccountStatus getAccountStatus() { return accountStatus; }
    public void setAccountStatus(AccountStatus accountStatus) { this.accountStatus = accountStatus; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<BankAccount> getBankAccounts() { return bankAccounts; }
    public void setBankAccounts(List<BankAccount> bankAccounts) { this.bankAccounts = bankAccounts; }

    private void updateFullName() {
        if (this.name != null && this.surname != null) {
            this.fullName = this.name + " " + this.surname;
        }
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        updateFullName();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        updateFullName();
    }
}
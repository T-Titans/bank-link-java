package com.banklink.controller;

import com.banklink.model.BankAccount;
import com.banklink.model.Transaction;
import com.banklink.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200", "http://localhost:5173"})
public class AccountController {

    @Autowired
    private AccountService accountService;

    // Get all accounts
    @GetMapping
    public ResponseEntity<List<BankAccount>> getAllAccounts() {
        try {
            List<BankAccount> accounts = accountService.getAllAccounts();
            return ResponseEntity.ok(accounts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get account details by ID
    @GetMapping("/{accountId}")
    public ResponseEntity<BankAccount> getAccount(@PathVariable String accountId) {
        try {
            Optional<BankAccount> account = accountService.getAccountById(accountId);
            if (account.isPresent()) {
                return ResponseEntity.ok(account.get());
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get account balance
    @GetMapping("/{accountId}/balance")
    public ResponseEntity<Double> getAccountBalance(@PathVariable String accountId) {
        try {
            Double balance = accountService.getAccountBalance(accountId);
            return ResponseEntity.ok(balance);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Create new account
    @PostMapping
    public ResponseEntity<BankAccount> createAccount(
            @RequestParam String accountId,
            @RequestParam String accountType,
            @RequestParam(required = false, defaultValue = "0.0") Double initialBalance) {
        try {
            BankAccount account = accountService.createAccount(accountId, accountType, initialBalance);
            return ResponseEntity.status(HttpStatus.CREATED).body(account);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Deposit endpoint
    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<String> deposit(@PathVariable String accountId, @RequestParam double amount) {
        try {
            BankAccount account = accountService.deposit(accountId, amount);
            return ResponseEntity.ok("Deposit successful. New balance: " + account.getFormattedBalance());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during deposit");
        }
    }

    // Withdraw endpoint
    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<String> withdraw(@PathVariable String accountId, @RequestParam double amount) {
        try {
            BankAccount account = accountService.withdraw(accountId, amount);
            return ResponseEntity.ok("Withdrawal successful. New balance: " + account.getFormattedBalance());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during withdrawal");
        }
    }

    // Transfer endpoint
    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(
            @RequestParam String fromAccountId,
            @RequestParam String toAccountId,
            @RequestParam double amount) {
        try {
            accountService.transfer(fromAccountId, toAccountId, amount);
            return ResponseEntity.ok("Transfer successful from " + fromAccountId + " to " + toAccountId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during transfer");
        }
    }

    // Get transaction history
    @GetMapping("/{accountId}/transactions")
    public ResponseEntity<List<Transaction>> getTransactionHistory(@PathVariable String accountId) {
        try {
            List<Transaction> transactions = accountService.getTransactionHistory(accountId);
            return ResponseEntity.ok(transactions);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Initialize default accounts
    @PostMapping("/initialize")
    public ResponseEntity<String> initializeDefaultAccounts() {
        try {
            accountService.initializeDefaultAccounts();
            return ResponseEntity.ok("Default accounts initialized successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while initializing accounts");
        }
    }

    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Account service is running");
    }
}
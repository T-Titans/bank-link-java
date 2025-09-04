package com.banklink.controller;

import com.banklink.model.BankAccount;
import com.banklink.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    // Get account details by ID
    @GetMapping("/{accountId}")
    public ResponseEntity<BankAccount> getAccount(@PathVariable String accountId) {
        BankAccount account = accountService.getAccountById(accountId);
        if (account != null) {
            return ResponseEntity.ok(account);
        }
        return ResponseEntity.notFound().build();
    }

    // Deposit endpoint
    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<String> deposit(@PathVariable String accountId, @RequestParam double amount) {
        accountService.deposit(accountId, amount);
        return ResponseEntity.ok("Deposit successful.");
    }

    // Withdraw endpoint
    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<String> withdraw(@PathVariable String accountId, @RequestParam double amount) {
        boolean success = accountService.withdraw(accountId, amount);
        if (success) {
            return ResponseEntity.ok("Withdrawal successful.");
        }
        return ResponseEntity.badRequest().body("Insufficient funds.");
    }
}
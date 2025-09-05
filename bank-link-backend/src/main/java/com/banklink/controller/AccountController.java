package com.banklink.controller;

import com.banklink.dto.TransferRequest;
import com.banklink.model.BankAccount;
import com.banklink.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "http://localhost:63342")
public class AccountController {

    @Autowired
    private AccountService accountService;

    // A single GET endpoint to get all accounts for the user.
    // This matches the fetchAccounts function in your script.js
    @GetMapping
    public ResponseEntity<Map<String, BankAccount>> getAllAccounts() {
        // Since we are not handling users yet, we return all accounts from the mock database
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    // Deposit endpoint - now accepts JSON body
    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody Map<String, Object> request) {
        String accountId = (String) request.get("accountId");
        double amount = ((Number) request.get("amount")).doubleValue();

        accountService.deposit(accountId, amount);
        return ResponseEntity.ok("Deposit successful.");
    }

    // Withdraw endpoint - now accepts JSON body
    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody Map<String, Object> request) {
        String accountId = (String) request.get("accountId");
        double amount = ((Number) request.get("amount")).doubleValue();

        boolean success = accountService.withdraw(accountId, amount);
        if (success) {
            return ResponseEntity.ok("Withdrawal successful.");
        }
        return ResponseEntity.badRequest().body("Insufficient funds.");
    }

    // Transfer endpoint - already uses @RequestBody
    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody TransferRequest transferRequest) {
        boolean success = accountService.transfer(
                transferRequest.getFromAccountId(),
                transferRequest.getToAccountId(),
                transferRequest.getAmount()
        );

        if (success) {
            return ResponseEntity.ok("Transfer successful.");
        }
        return ResponseEntity.badRequest().body("Transfer failed. Check accounts and balance.");
    }
}
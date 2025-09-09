package com.banklink.controller;

import com.banklink.dto.TransferRequest;
import com.banklink.model.BankAccount;
import com.banklink.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "http://localhost:63342")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping
    public ResponseEntity<Map<String, BankAccount>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

 // Deposit endpoint - now returns a JSON response
    // Deposit endpoint
    @PostMapping("/deposit")
    public ResponseEntity<BankAccount> deposit(@RequestBody Map<String, Object> request) {
        String accountId = (String) request.get("accountId");
        double amount = ((Number) request.get("amount")).doubleValue();

        accountService.deposit(accountId, amount);
        return ResponseEntity.ok(accountService.getAccountById(accountId));
    }

    // Withdraw endpoint
    @PostMapping("/withdraw")
    public ResponseEntity<Object> withdraw(@RequestBody Map<String, Object> request) {
        String accountId = (String) request.get("accountId");
        double amount = ((Number) request.get("amount")).doubleValue();

        boolean success = accountService.withdraw(accountId, amount);
        
        if (success) {
            return ResponseEntity.ok(accountService.getAccountById(accountId));
        }
        
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", "Insufficient funds.");
        return ResponseEntity.badRequest().body(errorResponse);
    }

    // Transfer endpoint
    @PostMapping("/transfer")
    public ResponseEntity<Object> transfer(@RequestBody TransferRequest transferRequest) {
        boolean success = accountService.transfer(
                transferRequest.getFromAccountId(),
                transferRequest.getToAccountId(),
                transferRequest.getAmount()
        );

        if (success) {
            Map<String, BankAccount> response = new HashMap<>();
            response.put("fromAccount", accountService.getAccountById(transferRequest.getFromAccountId()));
            response.put("toAccount", accountService.getAccountById(transferRequest.getToAccountId()));
            return ResponseEntity.ok(response);
        }
        
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", "Transfer failed. Check accounts and balance.");
        return ResponseEntity.badRequest().body(errorResponse);
    }
}
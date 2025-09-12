package com.banklink.controller;

import com.banklink.dto.LoginRequest;
import com.banklink.dto.RegisterRequest;
import com.banklink.model.User;
import com.banklink.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            User user = userService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());
            
            if (user != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Login successful");
                response.put("user", createUserResponse(user));
                
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Invalid email or password");
                
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Login failed: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        try {
            if (userService.existsByEmail(registerRequest.getEmail())) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Email already registered!");
                
                return ResponseEntity.badRequest().body(response);
            }

            if (userService.existsByIdNumber(registerRequest.getIdNumber())) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "ID Number already registered!");
                
                return ResponseEntity.badRequest().body(response);
            }

            User user = userService.createUser(registerRequest);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Registration successful");
            response.put("user", createUserResponse(user));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Registration failed: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }

    private Map<String, Object> createUserResponse(User user) {
        Map<String, Object> userResponse = new HashMap<>();
        userResponse.put("id", user.getId());
        userResponse.put("name", user.getName());
        userResponse.put("surname", user.getSurname());
        userResponse.put("fullName", user.getFullName());
        userResponse.put("email", user.getEmail());
        userResponse.put("idNumber", user.getIdNumber());
        userResponse.put("createdAt", user.getCreatedAt());
        
        Map<String, Object> accounts = new HashMap<>();
        
        Map<String, Object> checking = new HashMap<>();
        checking.put("name", "Cheque Account");
        checking.put("balance", 1000.00);
        accounts.put("ACC001", checking);
        
        Map<String, Object> savings = new HashMap<>();
        savings.put("name", "Savings Account");
        savings.put("balance", 1000.00);
        accounts.put("SAV001", savings);
        
        userResponse.put("accounts", accounts);
        
        return userResponse;
    }
}

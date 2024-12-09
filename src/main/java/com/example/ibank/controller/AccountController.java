package com.example.ibank.controller;

import com.example.ibank.dto.LoginRequest;
import com.example.ibank.model.Account;
import com.example.ibank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController 
@RequestMapping("/api") // Ensure this matches the base URL in the security configuration
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Account account) {
        accountService.register(account);
        return ResponseEntity.ok("Registration successful");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        if (loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
            return ResponseEntity.badRequest().body("Username and password are required.");
        }

        Account account = accountService.authenticateAndGetAccount(loginRequest.getUsername(), loginRequest.getPassword());
        if (account != null) {
            return ResponseEntity.ok(account); // Return the authenticated account
        }

        return ResponseEntity.status(401).body("Invalid credentials");
    }

}

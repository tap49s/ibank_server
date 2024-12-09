package com.example.ibank.service;

import com.example.ibank.model.Account;
import com.example.ibank.repository.AccountRepository;
import com.example.ibank.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account register(Account account) {
        // Check if an account exists with the same email
        if (accountRepository.findByEmail(account.getEmail()).isPresent()) {
            throw new IllegalArgumentException("An account with this email already exists.");
        }

        // Check if an account exists with the same cell number
        if (accountRepository.findByCellNumber(account.getCellNumber()).isPresent()) {
            throw new IllegalArgumentException("An account with this cellphone number already exists.");
        }

        // Check if an account exists with the same ID number
        if (accountRepository.findByIdNumber(account.getIdNumber()).isPresent()) {
            throw new IllegalArgumentException("An account with this ID number already exists.");
        }

        // Hash the password and save the account
        account.setPassword(MD5Util.hash(account.getPassword()));
        account.setBalance(23033.0);
        return accountRepository.save(account);
    }

    public boolean authenticate(String username, String password) {
        // Hash the provided password
        String hashedPassword = MD5Util.hash(password);

        // Find the account by email or cell number
        Optional<Account> account = accountRepository.findByEmailOrCellNumber(username, username);

        // Check if the account exists and the hashed password matches
        return account.isPresent() && account.get().getPassword().equals(hashedPassword);
    }

    public Account authenticateAndGetAccount(String username, String password) {
        // Hash the provided password
        String hashedPassword = MD5Util.hash(password);

        // Find the account by email or cell number
        Optional<Account> account = accountRepository.findByEmailOrCellNumber(username, username);

        // Check if the account exists and the hashed password matches
        if (account.isPresent() && account.get().getPassword().equals(hashedPassword)) {
            Account presentAccount = account.get();
            presentAccount.setDateOfBirth(presentAccount.deriveDateOfBirthFromIdNumber());
            return presentAccount;
        }
        return null; // Return null if authentication fails
    }

    // Get accountNumber by email (or cell number)
    public String getAccountNumberByEmail(String email) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Account not found for email: " + email));
        return account.getAccountNumber();
    }

    // Get accountNumber by cell number
    public String getAccountNumberByCellNumber(String cellNumber) {
        Account account = accountRepository.findByCellNumber(cellNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found for cell number: " + cellNumber));
        return account.getAccountNumber();
    }

    // Fetch account by email
    public Account getAccountByEmail(String email) {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Account not found for email: " + email));
    }
}

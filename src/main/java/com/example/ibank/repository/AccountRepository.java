package com.example.ibank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ibank.model.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmailOrCellNumber(String email, String cellNumber);
    Optional<Account> findByEmail(String email);
    Optional<Account> findByCellNumber(String cellNumber);
    Optional<Account> findByIdNumber(String idNumber);
    // Find account by accountNumber (String)
    Optional<Account> findByAccountNumber(String accountNumber);
}
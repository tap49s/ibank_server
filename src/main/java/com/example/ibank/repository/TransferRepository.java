package com.example.ibank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ibank.model.Transfer;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
    // Fetch received transactions by accountNumber (receiver)
    List<Transfer> findByAccountNumber(String accountNumber);

    // Fetch sent transactions by myReference (sender)
    List<Transfer> findByMyReference(String myReference);
}

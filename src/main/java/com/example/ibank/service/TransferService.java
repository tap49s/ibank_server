package com.example.ibank.service;

import com.example.ibank.model.Account;
import com.example.ibank.model.Transfer;
import com.example.ibank.repository.AccountRepository;
import com.example.ibank.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransferService {

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private AccountRepository accountRepository;

    public Transfer saveTransfer(Transfer transfer, String authenticatedUserEmail) {
        // Get sender's account using the authenticated user's email
        Account senderAccount = accountRepository.findByEmail(authenticatedUserEmail)
                .orElseThrow(() -> new IllegalArgumentException("Sender account not found."));

        // Get the receiver's account using accountNumber from the transfer
        Optional<Account> receiverAccountOpt = accountRepository.findByAccountNumber(transfer.getAccountNumber());
        if (receiverAccountOpt.isEmpty()) {
            throw new IllegalArgumentException("Receiver account does not exist.");
        }
        Account receiverAccount = receiverAccountOpt.get();

        // Prevent self-transfer
        if (senderAccount.getAccountNumber().equals(receiverAccount.getAccountNumber())) {
            throw new IllegalArgumentException("Cannot transfer money to the same account.");
        }

        // Check if sender has enough balance
        if (senderAccount.getBalance() < transfer.getAmount()) {
            throw new IllegalArgumentException("Insufficient funds in sender's account.");
        }

        // Update balances: Subtract from sender and add to receiver
        senderAccount.setBalance(senderAccount.getBalance() - transfer.getAmount());  // Subtract from sender
        receiverAccount.setBalance(receiverAccount.getBalance() + transfer.getAmount());  // Add to receiver

        // Save updated accounts
        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);

        // Set the transfer date and myReference, then save the transaction
        transfer.setTransferDate(LocalDateTime.now());
        transfer.setMyReference(senderAccount.getAccountNumber());  // Sender's account number is the myReference
        return transferRepository.save(transfer);  // Save the transfer record
    }

    // Get all received transactions by accountNumber
    public List<Transfer> getReceivedTransactions(String accountNumber) {
        return transferRepository.findByAccountNumber(accountNumber);
    }

    // Get all sent transactions by accountNumber (using myReference as the sender's account)
    public List<Transfer> getSentTransactions(String accountNumber) {
        return transferRepository.findByMyReference(accountNumber);
    }
}

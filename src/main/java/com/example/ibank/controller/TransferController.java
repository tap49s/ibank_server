package com.example.ibank.controller;

import com.example.ibank.model.Transfer;
import com.example.ibank.service.AccountService;
import com.example.ibank.service.TransferService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TransferController {

    @Autowired
    private TransferService transferService;

    @Autowired
    private AccountService accountService;

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody Transfer transfer) {
        try {
            // Get the authenticated user's email
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String authenticatedUserEmail = authentication.getName();

            // Call the service method
            Transfer savedTransfer = transferService.saveTransfer(transfer, authenticatedUserEmail);
            return ResponseEntity.ok(savedTransfer);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint to get both received and sent transactions
    @GetMapping("/transactions")
    public ResponseEntity<?> getTransactionsForAuthenticatedAccount() {
        try {
            // Get the authenticated user's email (from Basic Auth)
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName(); // Email (or username, depending on your auth configuration)

            // Fetch the accountNumber for the authenticated user
            String accountNumber = accountService.getAccountNumberByEmail(email); // Or use getAccountNumberByCellNumber
                                                                                  // if you're using cellNumber

            // Fetch received transactions (where accountNumber is the receiver)
            List<Transfer> receivedTransactions = transferService.getReceivedTransactions(accountNumber);

            // Fetch sent transactions (where myReference is the sender)
            List<Transfer> sentTransactions = transferService.getSentTransactions(accountNumber);

            // Return both received and sent transactions as part of the response
            return ResponseEntity.ok(new TransactionResponse(receivedTransactions, sentTransactions));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while fetching transactions.");
        }
    }

    // Response structure containing received and sent transactions
    public static class TransactionResponse {
        private List<Transfer> receivedTransactions;
        private List<Transfer> sentTransactions;

        public TransactionResponse(List<Transfer> receivedTransactions, List<Transfer> sentTransactions) {
            this.receivedTransactions = receivedTransactions;
            this.sentTransactions = sentTransactions;
        }

        public List<Transfer> getReceivedTransactions() {
            return receivedTransactions;
        }

        public void setReceivedTransactions(List<Transfer> receivedTransactions) {
            this.receivedTransactions = receivedTransactions;
        }

        public List<Transfer> getSentTransactions() {
            return sentTransactions;
        }

        public void setSentTransactions(List<Transfer> sentTransactions) {
            this.sentTransactions = sentTransactions;
        }
    }
}
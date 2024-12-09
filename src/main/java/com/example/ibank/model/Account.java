package com.example.ibank.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@Entity
public class Account {

    private static final Logger logger = LoggerFactory.getLogger(Account.class);  // Logger for the Account class

    @Id
    @Column(unique = true, nullable = false)
    private String accountNumber;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Cell number is required")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid cell number format") // E.164 format
    private String cellNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Province is required")
    private String province;

    @NotBlank(message = "Suburb is required")
    private String suburb;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Street number is required")
    private String streetNumber;

    @NotBlank(message = "Street name is required")
    private String streetName;

    @NotBlank(message = "ID number is required")
    @Size(min = 13, max = 13, message = "ID number must be exactly 13 characters")
    private String idNumber;

    @NotBlank(message = "Password is required")
    private String password;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private Double balance = 0.0;  // Add balance to track the current balance

    // PrePersist lifecycle method to set accountNumber and derive dateOfBirth
    @PrePersist
    @PreUpdate
    public void processDerivedFields() {
        if (this.accountNumber == null) {
            this.accountNumber = generateRandomAccountNumber();
        }
        this.dateOfBirth = deriveDateOfBirthFromIdNumber();
    }

    private String generateRandomAccountNumber() {
        // Generate a 10-digit account number
        long accountNumber = (long) (Math.random() * 1_000_000_0000L + 1_000_000_0000L); // 10-digit random number
        // Log the generated account number
        logger.info("Generated Account Number: {}", accountNumber);
        return String.format("%010d", accountNumber); // Format to ensure it is exactly 10 characters long
    }
    
    public LocalDate deriveDateOfBirthFromIdNumber() {
        if (this.idNumber == null || this.idNumber.length() != 13) {
            throw new IllegalArgumentException("Invalid ID number: Must be 13 characters");
        }

        String datePart = this.idNumber.substring(0, 6);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");

        LocalDate dob = LocalDate.parse(datePart, formatter);
        if (dob.isAfter(LocalDate.now())) {
            dob = dob.minusYears(100); // Adjust for individuals born in the 1900s
        }
        return dob;
    }
}

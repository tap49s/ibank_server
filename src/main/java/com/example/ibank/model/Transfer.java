package com.example.ibank.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Bank is required")
    private String bank;

    @NotBlank(message = "Account name is required")
    private String accountName;

    @NotBlank(message = "Account number is required")
    private String accountNumber;

    private String beneficiaryReference;

    @NotBlank(message = "Your reference is required")
    private String myReference;

    @NotNull(message = "Amount is required")  // Change to @NotNull
    @Positive(message = "Amount must be positive")  // Optionally, add @Positive for amounts > 0
    private Double amount;
    
    private LocalDateTime transferDate;
}

package com.banking.bankingProject.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class LoanPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Loan loan;

    @NotNull
    private BigDecimal paymentAmount;

    private BigDecimal principalAmount;

    private BigDecimal interestAmount;

    @NotNull
    private String paymentReference;

    @CreationTimestamp
    private LocalDateTime paymentDate;

    private String remarks;
}

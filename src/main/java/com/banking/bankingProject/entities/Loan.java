package com.banking.bankingProject.entities;

import com.banking.bankingProject.enums.LoanStatus;
import com.banking.bankingProject.enums.LoanType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String loanNumber;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Account account; // Account to which loan will be disbursed

    @NotNull
    @Enumerated(EnumType.STRING)
    private LoanType loanType;

    @NotNull
    private BigDecimal loanAmount;

    private BigDecimal interestRate;

    private Integer tenureMonths;

    private BigDecimal emiAmount;

    private BigDecimal outstandingAmount;

    @NotNull
    @Enumerated(EnumType.STRING)
    private LoanStatus loanStatus;

    @CreationTimestamp
    private LocalDateTime applicationDate;

    private LocalDateTime approvalDate;

    private LocalDateTime disbursementDate;

    private LocalDate nextEmiDate;

    private String remarks;
}

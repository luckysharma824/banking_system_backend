package com.banking.bankingProject.dto;

import com.banking.bankingProject.enums.LoanType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoanDto {

    @NotNull(message = "Customer ID is required")
    private String customerId;

    @NotNull(message = "Account number is required")
    private String accountNumber;

    @NotNull(message = "Loan type is required")
    private LoanType loanType;

    @NotNull(message = "Loan amount is required")
    private BigDecimal loanAmount;

    @NotNull(message = "Interest rate is required")
    private BigDecimal interestRate;

    @NotNull(message = "Tenure in months is required")
    private Integer tenureMonths;

    private String remarks;
}

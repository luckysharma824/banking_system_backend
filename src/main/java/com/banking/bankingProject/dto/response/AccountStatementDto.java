package com.banking.bankingProject.dto.response;

import com.banking.bankingProject.entities.Transaction;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AccountStatementDto {
    private String accountNumber;
    private String customerName;
    private String accountType;
    private LocalDateTime statementStartDate;
    private LocalDateTime statementEndDate;
    private BigDecimal openingBalance;
    private BigDecimal closingBalance;
    private BigDecimal totalCredits;
    private BigDecimal totalDebits;
    private Integer transactionCount;
    private List<Transaction> transactions;
}

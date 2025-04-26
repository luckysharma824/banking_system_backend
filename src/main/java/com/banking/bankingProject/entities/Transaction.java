package com.banking.bankingProject.entities;

import com.banking.bankingProject.enums.TransactionTypeEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity
@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long accountId;
    private BigDecimal amount;
    @NotNull
    @Enumerated(EnumType.STRING)
    private TransactionTypeEnum type; // "deposit" or "withdrawal"
    @NotEmpty
    private String txnId;
    @CreationTimestamp
    private LocalDateTime createdDate;
}

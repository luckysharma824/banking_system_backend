package com.banking.bankingProject.entities;

import com.banking.bankingProject.enums.AccountStatus;
import com.banking.bankingProject.enums.AccountTypeEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String accountNumber;
    private BigDecimal balance;
    @NotNull
    @Enumerated(EnumType.STRING)
    private AccountTypeEnum accountType;
    @ManyToOne
    private Customer customer;
    @NotNull
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;
    @CreationTimestamp
    private LocalDateTime createdDate;
    private LocalDateTime lastTransactionDate;
}

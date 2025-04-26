package com.banking.bankingProject.dto;

import com.banking.bankingProject.enums.AccountStatus;
import com.banking.bankingProject.enums.AccountTypeEnum;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountDto {
    private Long id;
    private String accountNumber;
    private BigDecimal balance;
    private AccountTypeEnum accountType;
    private String status;
    private AccountStatus accountStatus;
}

package com.banking.bankingProject.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class BeneficiaryDto {

    @NotEmpty(message = "Beneficiary name is required")
    private String beneficiaryName;

    @NotEmpty(message = "Beneficiary account number is required")
    private String beneficiaryAccountNumber;

    @NotEmpty(message = "Beneficiary bank name is required")
    private String beneficiaryBankName;

    private String beneficiaryBankCode;

    @NotEmpty(message = "Nickname is required")
    private String nickname;
}

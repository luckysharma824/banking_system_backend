package com.banking.bankingProject.dto;

import com.banking.bankingProject.enums.IdentityTypeEnum;
import lombok.Data;

@Data
public class IdentityDto {
    private Long id;
    private IdentityTypeEnum identityType;
    private String identityNumber;
}

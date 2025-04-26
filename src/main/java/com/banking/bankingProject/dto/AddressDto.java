package com.banking.bankingProject.dto;

import com.banking.bankingProject.enums.AddressTypeEnum;
import com.banking.bankingProject.enums.StateEnum;
import lombok.Data;

@Data
public class AddressDto {
    private Long id;
    private String line1;
    private String line2;
    private String line3;
    private StateEnum state;
    private String postalCode;
    private AddressTypeEnum addressType;
}

package com.banking.bankingProject.dto;

import lombok.Data;

import java.util.List;

@Data
public class CustomerDto {
    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String father;
    private String mother;
    private String email;
    private String phone;
    private String customerId;
    List<AddressDto> addresses;
    /*List<AccountDto> accounts;*/
    List<IdentityDto> identities;
}

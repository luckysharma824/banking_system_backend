package com.banking.bankingProject.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private List<RoleDto> roles;
}

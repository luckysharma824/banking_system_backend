package com.banking.bankingProject.dto;

import com.banking.bankingProject.enums.RoleEnum;
import lombok.Getter;

import java.util.Set;

@Getter
public class UserInfo {
    private final Integer userId;
    private final String username;
    private final Set<RoleEnum> roles;
    private final String email;
    private final String fullName;

    public UserInfo(Integer userId, String username, Set<RoleEnum> roles, String email, String fullName) {
        this.userId = userId;
        this.username = username;
        this.roles = roles;
        this.email = email;
        this.fullName = fullName;
    }
}
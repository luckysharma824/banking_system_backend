package com.banking.bankingProject.dto;

import com.banking.bankingProject.enums.RoleEnum;
import lombok.Data;

import java.util.List;

@Data
public class RoleDto {
    private RoleEnum name;
    private List<ModulePermissionDto> modulePermissions;
}

package com.banking.bankingProject.dto;

import com.banking.bankingProject.enums.ModuleEnum;
import com.banking.bankingProject.enums.PermissionEnum;
import lombok.Data;

import java.util.List;

@Data
public class ModulePermissionDto {
    private ModuleEnum moduleName;
    private List<PermissionEnum> permissions;
}

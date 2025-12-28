package com.banking.bankingProject.dto;

import com.banking.bankingProject.enums.RoleEnum;
import lombok.Data;

import java.util.Set;

@Data
public class EndpointSecurityDto {
    private Long id;
    private String urlPattern;
    private String httpMethod;
    private Set<RoleEnum> allowedRoles;
    private Integer priority;
    private Boolean permitAll;
    private Boolean enabled;
}

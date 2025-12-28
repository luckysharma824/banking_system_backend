package com.banking.bankingProject.entities;

import com.banking.bankingProject.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "endpoint_security")
public class EndpointSecurity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String urlPattern;

    @Column(name = "http_method")
    private String httpMethod; // GET, POST, PUT, DELETE, or null for all methods

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "endpoint_security_roles", joinColumns = @JoinColumn(name = "endpoint_security_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Set<RoleEnum> allowedRoles;

    @Column(nullable = false)
    private Integer priority; // Lower number = higher priority for matching

    @Column(nullable = false)
    private Boolean permitAll = false; // If true, no authentication required

    @Column(nullable = false)
    private Boolean enabled = true;
}

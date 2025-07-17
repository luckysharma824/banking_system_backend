package com.banking.bankingProject.entities;

import com.banking.bankingProject.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private RoleEnum name;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_module_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "module_permission_id")
    )
    private Set<ModulePermission> modulePermissions;
}

package com.banking.bankingProject.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
public class ModulePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private Module module;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "module_permission_permissions",
            joinColumns = @JoinColumn(name = "module_permission_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions;
}

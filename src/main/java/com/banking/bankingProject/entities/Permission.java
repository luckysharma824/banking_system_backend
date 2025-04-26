package com.banking.bankingProject.entities;

import com.banking.bankingProject.enums.PermissionEnum;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PermissionEnum name;

}

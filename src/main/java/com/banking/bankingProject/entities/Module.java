package com.banking.bankingProject.entities;

import com.banking.bankingProject.enums.ModuleEnum;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ModuleEnum name;
}

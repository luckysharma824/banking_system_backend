package com.banking.bankingProject.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String entityType; // Account, Transaction, Customer, etc.

    private Long entityId;

    private String action; // CREATE, UPDATE, DELETE, etc.

    private String performedBy; // User who performed the action

    private String ipAddress;

    @Column(columnDefinition = "TEXT")
    private String oldValue;

    @Column(columnDefinition = "TEXT")
    private String newValue;

    @CreationTimestamp
    private LocalDateTime timestamp;

    private String description;
}

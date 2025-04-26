package com.banking.bankingProject.entities;

import com.banking.bankingProject.enums.IdentityTypeEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class Identity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Enumerated(EnumType.STRING)
    private IdentityTypeEnum identityType;
    @NotEmpty
    private String identityNumber;
    @ManyToOne
    private Customer customer;
    @CreationTimestamp
    private LocalDateTime createdDate;
}

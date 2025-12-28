package com.banking.bankingProject.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class Beneficiary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String beneficiaryName;

    @NotEmpty
    private String beneficiaryAccountNumber;

    @NotEmpty
    private String beneficiaryBankName;

    private String beneficiaryBankCode; // IFSC or SWIFT code

    @NotEmpty
    private String nickname; // Friendly name for beneficiary

    @ManyToOne
    private Account account; // The account that added this beneficiary

    private Boolean isActive = true;

    @CreationTimestamp
    private LocalDateTime createdDate;

    private LocalDateTime lastUsedDate;
}

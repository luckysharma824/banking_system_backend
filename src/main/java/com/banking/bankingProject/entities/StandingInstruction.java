package com.banking.bankingProject.entities;

import com.banking.bankingProject.enums.StandingInstructionFrequency;
import com.banking.bankingProject.enums.StandingInstructionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class StandingInstruction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String instructionNumber;

    @ManyToOne
    private Account fromAccount;

    @NotEmpty
    private String toAccountNumber;

    private String toBeneficiaryName;

    @NotNull
    private BigDecimal amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StandingInstructionFrequency frequency;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDate nextExecutionDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StandingInstructionStatus status;

    @CreationTimestamp
    private LocalDateTime createdDate;

    private LocalDateTime lastExecutionDate;

    private Integer executionCount = 0;

    private Integer failedCount = 0;

    private String description;
}

package com.banking.bankingProject.dto;

import com.banking.bankingProject.enums.StandingInstructionFrequency;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class StandingInstructionDto {

    @NotEmpty(message = "From account number is required")
    private String fromAccountNumber;

    @NotEmpty(message = "To account number is required")
    private String toAccountNumber;

    private String toBeneficiaryName;

    @NotNull(message = "Amount is required")
    private BigDecimal amount;

    @NotNull(message = "Frequency is required")
    private StandingInstructionFrequency frequency;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    private LocalDate endDate;

    private String description;
}

package com.banking.bankingProject.entities;

import com.banking.bankingProject.enums.AddressTypeEnum;
import com.banking.bankingProject.enums.StateEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    private String line1;
    private String line2;
    private String line3;
    @NotNull
    @Enumerated(EnumType.STRING)
    private StateEnum state;
    @NotEmpty
    private String postalCode;
    @NotNull
    @Enumerated(EnumType.STRING)
    private AddressTypeEnum addressType;
    @ManyToOne
    private Customer customer;
    @CreationTimestamp
    private LocalDateTime createdDate;
}

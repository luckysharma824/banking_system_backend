package com.banking.bankingProject.repositories;

import com.banking.bankingProject.entities.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {

    // Find all beneficiaries for an account
    List<Beneficiary> findByAccount_AccountNumber(String accountNumber);

    // Find active beneficiaries for an account
    List<Beneficiary> findByAccount_AccountNumberAndIsActiveTrue(String accountNumber);

    // Find beneficiary by account and beneficiary account number
    Optional<Beneficiary> findByAccount_AccountNumberAndBeneficiaryAccountNumber(
            String accountNumber, String beneficiaryAccountNumber);

    // Check if beneficiary exists
    boolean existsByAccount_AccountNumberAndBeneficiaryAccountNumber(
            String accountNumber, String beneficiaryAccountNumber);
}

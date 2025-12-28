package com.banking.bankingProject.services;

import com.banking.bankingProject.dto.BeneficiaryDto;
import com.banking.bankingProject.entities.Account;
import com.banking.bankingProject.entities.Beneficiary;
import com.banking.bankingProject.exception.BankServiceException;
import com.banking.bankingProject.repositories.AccountRepository;
import com.banking.bankingProject.repositories.BeneficiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BeneficiaryService {

    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

    @Autowired
    private AccountRepository accountRepository;

    public Beneficiary addBeneficiary(String accountNumber, BeneficiaryDto beneficiaryDto) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new BankServiceException("EC-101", "Account not found", null);
        }

        // Check if beneficiary already exists
        if (beneficiaryRepository.existsByAccount_AccountNumberAndBeneficiaryAccountNumber(
                accountNumber, beneficiaryDto.getBeneficiaryAccountNumber())) {
            throw new BankServiceException("EC-102", "Beneficiary already exists", null);
        }

        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setBeneficiaryName(beneficiaryDto.getBeneficiaryName());
        beneficiary.setBeneficiaryAccountNumber(beneficiaryDto.getBeneficiaryAccountNumber());
        beneficiary.setBeneficiaryBankName(beneficiaryDto.getBeneficiaryBankName());
        beneficiary.setBeneficiaryBankCode(beneficiaryDto.getBeneficiaryBankCode());
        beneficiary.setNickname(beneficiaryDto.getNickname());
        beneficiary.setAccount(account);
        beneficiary.setIsActive(true);

        return beneficiaryRepository.save(beneficiary);
    }

    public List<Beneficiary> getAllBeneficiaries(String accountNumber) {
        return beneficiaryRepository.findByAccount_AccountNumber(accountNumber);
    }

    public List<Beneficiary> getActiveBeneficiaries(String accountNumber) {
        return beneficiaryRepository.findByAccount_AccountNumberAndIsActiveTrue(accountNumber);
    }

    public Beneficiary updateBeneficiary(Long beneficiaryId, BeneficiaryDto beneficiaryDto) {
        Beneficiary beneficiary = beneficiaryRepository.findById(beneficiaryId)
                .orElseThrow(() -> new BankServiceException("EC-103", "Beneficiary not found", null));

        beneficiary.setBeneficiaryName(beneficiaryDto.getBeneficiaryName());
        beneficiary.setBeneficiaryBankName(beneficiaryDto.getBeneficiaryBankName());
        beneficiary.setBeneficiaryBankCode(beneficiaryDto.getBeneficiaryBankCode());
        beneficiary.setNickname(beneficiaryDto.getNickname());

        return beneficiaryRepository.save(beneficiary);
    }

    public void deleteBeneficiary(Long beneficiaryId) {
        Beneficiary beneficiary = beneficiaryRepository.findById(beneficiaryId)
                .orElseThrow(() -> new BankServiceException("EC-103", "Beneficiary not found", null));
        beneficiary.setIsActive(false);
        beneficiaryRepository.save(beneficiary);
    }

    public void updateLastUsedDate(String accountNumber, String beneficiaryAccountNumber) {
        Beneficiary beneficiary = beneficiaryRepository
                .findByAccount_AccountNumberAndBeneficiaryAccountNumber(accountNumber, beneficiaryAccountNumber)
                .orElse(null);
        if (beneficiary != null) {
            beneficiary.setLastUsedDate(LocalDateTime.now());
            beneficiaryRepository.save(beneficiary);
        }
    }
}

package com.banking.bankingProject.controllers;

import com.banking.bankingProject.dto.BeneficiaryDto;
import com.banking.bankingProject.entities.Beneficiary;
import com.banking.bankingProject.exception.BankServiceException;
import com.banking.bankingProject.services.BeneficiaryService;
import com.banking.bankingProject.util.ResponseHandler;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/beneficiaries")
public class BeneficiaryController {

    @Autowired
    private BeneficiaryService beneficiaryService;

    @PostMapping("/add")
    public ResponseEntity<Object> addBeneficiary(
            @RequestParam String accountNumber,
            @Valid @RequestBody BeneficiaryDto beneficiaryDto) {
        try {
            Beneficiary beneficiary = beneficiaryService.addBeneficiary(accountNumber, beneficiaryDto);
            return ResponseHandler.handle(beneficiary, "Beneficiary added successfully", true, HttpStatus.OK);
        } catch (BankServiceException ex) {
            return ResponseHandler.handle(ex.getErrorMessage(), false, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<Object> getAllBeneficiaries(@PathVariable String accountNumber) {
        try {
            List<Beneficiary> beneficiaries = beneficiaryService.getAllBeneficiaries(accountNumber);
            return ResponseHandler.handle(beneficiaries, "Beneficiaries retrieved successfully", true, HttpStatus.OK);
        } catch (BankServiceException ex) {
            return ResponseHandler.handle(ex.getErrorMessage(), false, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{accountNumber}/active")
    public ResponseEntity<Object> getActiveBeneficiaries(@PathVariable String accountNumber) {
        try {
            List<Beneficiary> beneficiaries = beneficiaryService.getActiveBeneficiaries(accountNumber);
            return ResponseHandler.handle(beneficiaries, "Active beneficiaries retrieved successfully", true,
                    HttpStatus.OK);
        } catch (BankServiceException ex) {
            return ResponseHandler.handle(ex.getErrorMessage(), false, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{beneficiaryId}")
    public ResponseEntity<Object> updateBeneficiary(
            @PathVariable Long beneficiaryId,
            @Valid @RequestBody BeneficiaryDto beneficiaryDto) {
        try {
            Beneficiary beneficiary = beneficiaryService.updateBeneficiary(beneficiaryId, beneficiaryDto);
            return ResponseHandler.handle(beneficiary, "Beneficiary updated successfully", true, HttpStatus.OK);
        } catch (BankServiceException ex) {
            return ResponseHandler.handle(ex.getErrorMessage(), false, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{beneficiaryId}")
    public ResponseEntity<Object> deleteBeneficiary(@PathVariable Long beneficiaryId) {
        try {
            beneficiaryService.deleteBeneficiary(beneficiaryId);
            return ResponseHandler.handle(null, "Beneficiary deleted successfully", true, HttpStatus.OK);
        } catch (BankServiceException ex) {
            return ResponseHandler.handle(ex.getErrorMessage(), false, HttpStatus.BAD_REQUEST);
        }
    }
}

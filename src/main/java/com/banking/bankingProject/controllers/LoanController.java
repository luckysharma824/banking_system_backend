package com.banking.bankingProject.controllers;

import com.banking.bankingProject.dto.LoanDto;
import com.banking.bankingProject.entities.Loan;
import com.banking.bankingProject.entities.LoanPayment;
import com.banking.bankingProject.exception.BankServiceException;
import com.banking.bankingProject.services.LoanService;
import com.banking.bankingProject.util.ResponseHandler;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/loans")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @PostMapping("/apply")
    public ResponseEntity<Object> applyForLoan(@Valid @RequestBody LoanDto loanDto) {
        try {
            Loan loan = loanService.applyForLoan(loanDto);
            return ResponseHandler.handle(loan, "Loan application submitted successfully", true, HttpStatus.OK);
        } catch (BankServiceException ex) {
            return ResponseHandler.handle(ex.getErrorMessage(), false, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/approve/{loanNumber}")
    public ResponseEntity<Object> approveLoan(@PathVariable String loanNumber) {
        try {
            Loan loan = loanService.approveLoan(loanNumber);
            return ResponseHandler.handle(loan, "Loan approved successfully", true, HttpStatus.OK);
        } catch (BankServiceException ex) {
            return ResponseHandler.handle(ex.getErrorMessage(), false, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/reject/{loanNumber}")
    public ResponseEntity<Object> rejectLoan(@PathVariable String loanNumber, @RequestParam String remarks) {
        try {
            Loan loan = loanService.rejectLoan(loanNumber, remarks);
            return ResponseHandler.handle(loan, "Loan rejected", true, HttpStatus.OK);
        } catch (BankServiceException ex) {
            return ResponseHandler.handle(ex.getErrorMessage(), false, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/disburse/{loanNumber}")
    public ResponseEntity<Object> disburseLoan(@PathVariable String loanNumber) {
        try {
            Loan loan = loanService.disburseLoan(loanNumber);
            return ResponseHandler.handle(loan, "Loan disbursed successfully", true, HttpStatus.OK);
        } catch (BankServiceException ex) {
            return ResponseHandler.handle(ex.getErrorMessage(), false, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/payment/{loanNumber}")
    public ResponseEntity<Object> makePayment(@PathVariable String loanNumber, @RequestParam BigDecimal amount) {
        try {
            LoanPayment payment = loanService.makePayment(loanNumber, amount);
            return ResponseHandler.handle(payment, "Payment processed successfully", true, HttpStatus.OK);
        } catch (BankServiceException ex) {
            return ResponseHandler.handle(ex.getErrorMessage(), false, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Object> getLoansByCustomer(@PathVariable String customerId) {
        try {
            List<Loan> loans = loanService.getLoansByCustomer(customerId);
            return ResponseHandler.handle(loans, "Loans retrieved successfully", true, HttpStatus.OK);
        } catch (BankServiceException ex) {
            return ResponseHandler.handle(ex.getErrorMessage(), false, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{loanNumber}")
    public ResponseEntity<Object> getLoanDetails(@PathVariable String loanNumber) {
        try {
            Loan loan = loanService.getLoanDetails(loanNumber);
            return ResponseHandler.handle(loan, "Loan details retrieved successfully", true, HttpStatus.OK);
        } catch (BankServiceException ex) {
            return ResponseHandler.handle(ex.getErrorMessage(), false, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/payments/{loanNumber}")
    public ResponseEntity<Object> getLoanPaymentHistory(@PathVariable String loanNumber) {
        try {
            List<LoanPayment> payments = loanService.getLoanPaymentHistory(loanNumber);
            return ResponseHandler.handle(payments, "Payment history retrieved successfully", true, HttpStatus.OK);
        } catch (BankServiceException ex) {
            return ResponseHandler.handle(ex.getErrorMessage(), false, HttpStatus.BAD_REQUEST);
        }
    }
}

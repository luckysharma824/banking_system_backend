package com.banking.bankingProject.controllers;

import com.banking.bankingProject.entities.Transaction;
import com.banking.bankingProject.enums.TransactionTypeEnum;
import com.banking.bankingProject.exception.BankServiceException;
import com.banking.bankingProject.services.TransactionService;
import com.banking.bankingProject.util.ResponseHandler;
import com.banking.bankingProject.util.Utility;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<Object> deposit(@RequestParam String accountNumber, @RequestParam BigDecimal amount) {
        String txnId = "TXN_" + Utility.getUuid("");
        try {
            Transaction transaction = transactionService.deposit(accountNumber, amount, txnId, TransactionTypeEnum.DEPOSIT);
            return ResponseHandler.handle(transaction, "Amount successfully deposited", true, HttpStatus.OK);
        } catch (BankServiceException ex) {
            return ResponseHandler.handle(ex.getErrorMessage(), false, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Object> withdraw(@RequestParam String accountNumber, @RequestParam BigDecimal amount) {
        String txnId = "TXN_" + Utility.getUuid("");
        try {
            Transaction transaction = transactionService.withdraw(accountNumber, amount, txnId, TransactionTypeEnum.WITHDRAW);
            return ResponseHandler.handle(transaction, "Amount successfully withdrawn", true, HttpStatus.OK);
        } catch (BankServiceException ex) {
            return ResponseHandler.handle(ex.getErrorMessage(), false, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<Object> transfer(@RequestParam String fromAccount, @RequestParam String toAccount, @RequestParam BigDecimal amount) {
        String txnId = transactionService.transfer(fromAccount, toAccount, amount);
        try {
            return ResponseHandler.handle(txnId, "Amount successfully transferred", true, HttpStatus.OK);
        } catch (BankServiceException ex) {
            return ResponseHandler.handle(ex.getErrorMessage(), false, HttpStatus.BAD_REQUEST);
        }
    }
}

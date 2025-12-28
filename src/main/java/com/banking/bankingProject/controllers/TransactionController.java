package com.banking.bankingProject.controllers;

import com.banking.bankingProject.entities.Transaction;
import com.banking.bankingProject.enums.TransactionTypeEnum;
import com.banking.bankingProject.exception.BankServiceException;
import com.banking.bankingProject.services.TransactionService;
import com.banking.bankingProject.util.ResponseHandler;
import com.banking.bankingProject.util.Utility;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
            Transaction transaction = transactionService.deposit(accountNumber, amount, txnId,
                    TransactionTypeEnum.DEPOSIT);
            return ResponseHandler.handle(transaction, "Amount successfully deposited", true, HttpStatus.OK);
        } catch (BankServiceException ex) {
            return ResponseHandler.handle(ex.getErrorMessage(), false, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Object> withdraw(@RequestParam String accountNumber, @RequestParam BigDecimal amount) {
        String txnId = "TXN_" + Utility.getUuid("");
        try {
            Transaction transaction = transactionService.withdraw(accountNumber, amount, txnId,
                    TransactionTypeEnum.WITHDRAW);
            return ResponseHandler.handle(transaction, "Amount successfully withdrawn", true, HttpStatus.OK);
        } catch (BankServiceException ex) {
            return ResponseHandler.handle(ex.getErrorMessage(), false, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<Object> transfer(@RequestParam String fromAccount, @RequestParam String toAccount,
            @RequestParam BigDecimal amount) {
        String txnId = transactionService.transfer(fromAccount, toAccount, amount);
        try {
            return ResponseHandler.handle(txnId, "Amount successfully transferred", true, HttpStatus.OK);
        } catch (BankServiceException ex) {
            return ResponseHandler.handle(ex.getErrorMessage(), false, HttpStatus.BAD_REQUEST);
        }
    }

    // Get transaction history for an account
    @GetMapping("/history/{accountNumber}")
    public ResponseEntity<Object> getTransactionHistory(@PathVariable String accountNumber) {
        try {
            List<Transaction> transactions = transactionService.getTransactionHistory(accountNumber);
            return ResponseHandler.handle(transactions, "Transaction history retrieved successfully", true,
                    HttpStatus.OK);
        } catch (BankServiceException ex) {
            return ResponseHandler.handle(ex.getErrorMessage(), false, HttpStatus.BAD_REQUEST);
        }
    }

    // Get paginated transaction history
    @GetMapping("/history/{accountNumber}/paginated")
    public ResponseEntity<Object> getTransactionHistoryPaginated(
            @PathVariable String accountNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<Transaction> transactions = transactionService.getTransactionHistoryPaginated(accountNumber, page,
                    size);
            return ResponseHandler.handle(transactions, "Transaction history retrieved successfully", true,
                    HttpStatus.OK);
        } catch (BankServiceException ex) {
            return ResponseHandler.handle(ex.getErrorMessage(), false, HttpStatus.BAD_REQUEST);
        }
    }

    // Get transactions by date range
    @GetMapping("/history/{accountNumber}/dateRange")
    public ResponseEntity<Object> getTransactionsByDateRange(
            @PathVariable String accountNumber,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            List<Transaction> transactions = transactionService.getTransactionsByDateRange(accountNumber, startDate,
                    endDate);
            return ResponseHandler.handle(transactions, "Transactions for date range retrieved successfully", true,
                    HttpStatus.OK);
        } catch (BankServiceException ex) {
            return ResponseHandler.handle(ex.getErrorMessage(), false, HttpStatus.BAD_REQUEST);
        }
    }

    // Get transactions by type
    @GetMapping("/history/{accountNumber}/type/{type}")
    public ResponseEntity<Object> getTransactionsByType(
            @PathVariable String accountNumber,
            @PathVariable TransactionTypeEnum type) {
        try {
            List<Transaction> transactions = transactionService.getTransactionsByType(accountNumber, type);
            return ResponseHandler.handle(transactions, "Transactions by type retrieved successfully", true,
                    HttpStatus.OK);
        } catch (BankServiceException ex) {
            return ResponseHandler.handle(ex.getErrorMessage(), false, HttpStatus.BAD_REQUEST);
        }
    }

    // Get transactions by txnId
    @GetMapping("/txn/{txnId}")
    public ResponseEntity<Object> getTransactionsByTxnId(@PathVariable String txnId) {
        try {
            List<Transaction> transactions = transactionService.getTransactionsByTxnId(txnId);
            return ResponseHandler.handle(transactions, "Transactions retrieved successfully", true, HttpStatus.OK);
        } catch (BankServiceException ex) {
            return ResponseHandler.handle(ex.getErrorMessage(), false, HttpStatus.BAD_REQUEST);
        }
    }

    // Get recent transactions
    @GetMapping("/recent/{accountNumber}")
    public ResponseEntity<Object> getRecentTransactions(@PathVariable String accountNumber) {
        try {
            List<Transaction> transactions = transactionService.getRecentTransactions(accountNumber);
            return ResponseHandler.handle(transactions, "Recent transactions retrieved successfully", true,
                    HttpStatus.OK);
        } catch (BankServiceException ex) {
            return ResponseHandler.handle(ex.getErrorMessage(), false, HttpStatus.BAD_REQUEST);
        }
    }
}

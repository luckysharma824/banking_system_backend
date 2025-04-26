package com.banking.bankingProject.controllers;

import com.banking.bankingProject.dto.AccountDto;
import com.banking.bankingProject.entities.Account;
import com.banking.bankingProject.services.AccountService;
import com.banking.bankingProject.util.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createAccount(@RequestParam String customerId, @RequestBody AccountDto account) {
        //return accountService.createAccount(customerId, account);
        Account account1 = accountService.createAccount(customerId, account);
        return ResponseHandler.handle(account1, "Account Created Successfully", true, HttpStatus.OK);
    }

    @GetMapping("/balance/{accountNumber}")
    public ResponseEntity<Object> checkBalance(@PathVariable String accountNumber) {
        BigDecimal bigDecimal = accountService.checkBalance(accountNumber);
        return ResponseHandler.handle(bigDecimal, "Current Balance is: " + bigDecimal, true, HttpStatus.OK);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<Object> getAccounts(@PathVariable String customerId) {
        List<Account> accounts = accountService.findAccount(customerId);
        return ResponseHandler.handle(accounts, "Accounts Fetched", true, HttpStatus.OK);
    }

}

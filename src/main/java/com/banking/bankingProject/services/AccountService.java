package com.banking.bankingProject.services;

import com.banking.bankingProject.dto.AccountDto;
import com.banking.bankingProject.entities.Account;
import com.banking.bankingProject.entities.Customer;
import com.banking.bankingProject.enums.AccountStatus;
import com.banking.bankingProject.repositories.AccountRepository;
import com.banking.bankingProject.repositories.CustomerRepository;
import com.banking.bankingProject.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CustomerRepository customerRepository;

    public Account createAccount(String customerId, AccountDto accountDto) {
        Customer customer = customerRepository.findByCustomerId(customerId);
        if (customer != null) {
            Account account = new Account();
            account.setAccountNumber("ACC_" + Utility.getUuid(""));
            account.setAccountType(accountDto.getAccountType());
            account.setAccountStatus(accountDto.getAccountStatus());
            account.setBalance(accountDto.getBalance());
            account.setCustomer(customer);
            return accountRepository.save(account);
        }
        return null;
    }

    public BigDecimal checkBalance(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        return account != null ? account.getBalance() : null;
    }

    public List<Account> findAccount(String customerId) {
        return accountRepository.findByCustomer_CustomerId(customerId);
    }

    public Account changeAccountStatus(String accountNumber, String status) {
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("Invalid account status");
        } else {
            AccountStatus.getAccountStatus(status);
        }
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new RuntimeException("Account not found");
        }
        account.setAccountStatus(AccountStatus.getAccountStatus(status.toUpperCase()));
        return accountRepository.save(account);
    }

    public Account freezeAccount(String accountNumber) {

        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new RuntimeException("Account not found");
        }
        account.setAccountStatus(com.banking.bankingProject.enums.AccountStatus.FROZEN);
        return accountRepository.save(account);
    }

    public Account unfreezeAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new RuntimeException("Account not found");
        }
        account.setAccountStatus(com.banking.bankingProject.enums.AccountStatus.ACTIVE);
        return accountRepository.save(account);
    }

    public Account closeAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new RuntimeException("Account not found");
        }
        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new RuntimeException("Cannot close account with non-zero balance");
        }
        account.setAccountStatus(com.banking.bankingProject.enums.AccountStatus.CLOSED);
        return accountRepository.save(account);
    }

    public Account activateAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new RuntimeException("Account not found");
        }
        account.setAccountStatus(com.banking.bankingProject.enums.AccountStatus.ACTIVE);
        return accountRepository.save(account);
    }

}

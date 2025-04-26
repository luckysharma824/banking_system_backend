package com.banking.bankingProject.services;

import com.banking.bankingProject.dto.AccountDto;
import com.banking.bankingProject.entities.Account;
import com.banking.bankingProject.entities.Customer;
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

}

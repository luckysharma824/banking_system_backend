package com.banking.bankingProject.services;

import com.banking.bankingProject.entities.Account;
import com.banking.bankingProject.entities.Transaction;
import com.banking.bankingProject.enums.AccountStatus;
import com.banking.bankingProject.enums.TransactionTypeEnum;
import com.banking.bankingProject.exception.BankServiceException;
import com.banking.bankingProject.repositories.AccountRepository;
import com.banking.bankingProject.repositories.TransactionRepository;
import com.banking.bankingProject.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    public Account getAccount(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    public Transaction deposit(String accountNumber, BigDecimal amount, String txnId, TransactionTypeEnum type) {
        Account account = getAccount(accountNumber);
        if (AccountStatus.INACTIVE.equals(account.getAccountStatus())) {
            throw new BankServiceException("EC-100", "To Account Not Found", null);
        }
        account.setBalance(account.getBalance().add(amount));
        account.setLastTransactionDate(LocalDateTime.now());
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAccountId(account.getId());
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setTxnId(txnId);
        return transactionRepository.save(transaction);
    }

    public Transaction withdraw(String accountNumber, BigDecimal amount, String txnId, TransactionTypeEnum type) {
        Account account = getAccount(accountNumber);
        if (AccountStatus.INACTIVE.equals(account.getAccountStatus())) {
            throw new BankServiceException("EC-100", "From Account Not Found", null);
        }
        if (account.getBalance().compareTo(amount) >= 0) {
            account.setBalance(account.getBalance().subtract(amount));
            account.setLastTransactionDate(LocalDateTime.now());
            accountRepository.save(account);

            Transaction transaction = new Transaction();
            transaction.setAccountId(account.getId());
            transaction.setAmount(amount);
            transaction.setType(type);
            transaction.setTxnId(txnId);
            return transactionRepository.save(transaction);
        }
        return null;
    }

    public String transfer(String fromAccount, String toAccount, BigDecimal amount) {
        String txnId = "TXN_" + Utility.getUuid("");
        withdraw(fromAccount, amount, txnId, TransactionTypeEnum.TRANSFER_OUT);
        deposit(toAccount, amount, txnId, TransactionTypeEnum.TRANSFER_IN);
        return txnId;
    }
}

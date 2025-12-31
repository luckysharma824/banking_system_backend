package com.banking.bankingProject.services;

import com.banking.bankingProject.entities.Account;
import com.banking.bankingProject.entities.Transaction;
import com.banking.bankingProject.enums.AccountStatus;
import com.banking.bankingProject.enums.TransactionTypeEnum;
import com.banking.bankingProject.exception.BankServiceException;
import com.banking.bankingProject.repositories.AccountRepository;
import com.banking.bankingProject.repositories.TransactionRepository;
import com.banking.bankingProject.util.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
        if (AccountStatus.INACTIVE.equals(account.getAccountStatus()) ||
                AccountStatus.FROZEN.equals(account.getAccountStatus()) ||
                AccountStatus.CLOSED.equals(account.getAccountStatus())) {
            throw new BankServiceException("EC-100", "Account is not active for transactions", null);
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
        if (AccountStatus.INACTIVE.equals(account.getAccountStatus()) ||
                AccountStatus.FROZEN.equals(account.getAccountStatus()) ||
                AccountStatus.CLOSED.equals(account.getAccountStatus())) {
            throw new BankServiceException("EC-100", "Account is not active for transactions", null);
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
        throw new BankServiceException("EC-104", "Insufficient balance", null);
    }

    public String transfer(String fromAccount, String toAccount, BigDecimal amount) {
        // Generate sortable transaction ID with timestamp and check digit
        String txnId = IdGenerator.generateTransactionId("0001");
        withdraw(fromAccount, amount, txnId, TransactionTypeEnum.TRANSFER_OUT);
        deposit(toAccount, amount, txnId, TransactionTypeEnum.TRANSFER_IN);
        return txnId;
    }

    // Get transaction history for an account
    public List<Transaction> getTransactionHistory(String accountNumber) {
        Account account = getAccount(accountNumber);
        if (account == null) {
            throw new BankServiceException("EC-101", "Account Not Found", null);
        }
        return transactionRepository.findByAccountIdOrderByCreatedDateDesc(account.getId());
    }

    // Get paginated transaction history
    public Page<Transaction> getTransactionHistoryPaginated(String accountNumber, int page, int size) {
        Account account = getAccount(accountNumber);
        if (account == null) {
            throw new BankServiceException("EC-101", "Account Not Found", null);
        }
        Pageable pageable = PageRequest.of(page, size);
        return transactionRepository.findByAccountIdOrderByCreatedDateDesc(account.getId(), pageable);
    }

    // Get transactions by date range
    public List<Transaction> getTransactionsByDateRange(String accountNumber, LocalDateTime startDate,
            LocalDateTime endDate) {
        Account account = getAccount(accountNumber);
        if (account == null) {
            throw new BankServiceException("EC-101", "Account Not Found", null);
        }
        return transactionRepository.findByAccountIdAndDateRange(account.getId(), startDate, endDate);
    }

    // Get transactions by type
    public List<Transaction> getTransactionsByType(String accountNumber, TransactionTypeEnum type) {
        Account account = getAccount(accountNumber);
        if (account == null) {
            throw new BankServiceException("EC-101", "Account Not Found", null);
        }
        return transactionRepository.findByAccountIdAndTypeOrderByCreatedDateDesc(account.getId(), type);
    }

    // Get transactions by txnId (useful for transfer tracking)
    public List<Transaction> getTransactionsByTxnId(String txnId) {
        return transactionRepository.findByTxnId(txnId);
    }

    // Get last N transactions
    public List<Transaction> getRecentTransactions(String accountNumber) {
        Account account = getAccount(accountNumber);
        if (account == null) {
            throw new BankServiceException("EC-101", "Account Not Found", null);
        }
        return transactionRepository.findTop10ByAccountIdOrderByCreatedDateDesc(account.getId());
    }
}

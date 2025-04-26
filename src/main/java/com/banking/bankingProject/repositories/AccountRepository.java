package com.banking.bankingProject.repositories;

import com.banking.bankingProject.entities.Account;
import com.banking.bankingProject.enums.AccountStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
public interface AccountRepository extends JpaRepository<Account,Long> {
    Account findByAccountNumber(String accountNumber);

    @Modifying
    @Query("UPDATE Account a SET a.accountStatus = :status WHERE a.lastTransactionDate < :date")
    int updateAccountStatusByLastTransactionDate(AccountStatus status, LocalDateTime date);

    List<Account> findByCustomer_CustomerId(String customerId);

}

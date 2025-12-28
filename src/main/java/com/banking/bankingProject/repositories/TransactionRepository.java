package com.banking.bankingProject.repositories;

import com.banking.bankingProject.entities.Transaction;
import com.banking.bankingProject.enums.TransactionTypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Find all transactions by account ID
    List<Transaction> findByAccountIdOrderByCreatedDateDesc(Long accountId);

    // Find transactions by account ID with pagination
    Page<Transaction> findByAccountIdOrderByCreatedDateDesc(Long accountId, Pageable pageable);

    // Find transactions by account ID and date range
    @Query("SELECT t FROM Transaction t WHERE t.accountId = :accountId AND t.createdDate BETWEEN :startDate AND :endDate ORDER BY t.createdDate DESC")
    List<Transaction> findByAccountIdAndDateRange(@Param("accountId") Long accountId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // Find transactions by transaction type
    List<Transaction> findByAccountIdAndTypeOrderByCreatedDateDesc(Long accountId, TransactionTypeEnum type);

    // Find transactions by txnId
    List<Transaction> findByTxnId(String txnId);

    // Get total amount by account and transaction type
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.accountId = :accountId AND t.type = :type")
    BigDecimal getTotalAmountByAccountAndType(@Param("accountId") Long accountId,
            @Param("type") TransactionTypeEnum type);

    // Get last N transactions
    List<Transaction> findTop10ByAccountIdOrderByCreatedDateDesc(Long accountId);
}

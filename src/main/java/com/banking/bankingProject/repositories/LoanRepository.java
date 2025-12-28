package com.banking.bankingProject.repositories;

import com.banking.bankingProject.entities.Loan;
import com.banking.bankingProject.enums.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    Loan findByLoanNumber(String loanNumber);

    List<Loan> findByCustomer_CustomerId(String customerId);

    List<Loan> findByCustomer_CustomerIdAndLoanStatus(String customerId, LoanStatus status);

    List<Loan> findByLoanStatus(LoanStatus status);
}

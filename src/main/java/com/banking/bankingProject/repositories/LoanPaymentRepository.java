package com.banking.bankingProject.repositories;

import com.banking.bankingProject.entities.LoanPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface LoanPaymentRepository extends JpaRepository<LoanPayment, Long> {

    List<LoanPayment> findByLoan_LoanNumberOrderByPaymentDateDesc(String loanNumber);

    @Query("SELECT SUM(lp.paymentAmount) FROM LoanPayment lp WHERE lp.loan.loanNumber = :loanNumber")
    BigDecimal getTotalPaymentsByLoanNumber(@Param("loanNumber") String loanNumber);
}

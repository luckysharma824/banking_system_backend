package com.banking.bankingProject.services;

import com.banking.bankingProject.dto.LoanDto;
import com.banking.bankingProject.entities.Account;
import com.banking.bankingProject.entities.Customer;
import com.banking.bankingProject.entities.Loan;
import com.banking.bankingProject.entities.LoanPayment;
import com.banking.bankingProject.enums.LoanStatus;
import com.banking.bankingProject.exception.BankServiceException;
import com.banking.bankingProject.repositories.AccountRepository;
import com.banking.bankingProject.repositories.CustomerRepository;
import com.banking.bankingProject.repositories.LoanPaymentRepository;
import com.banking.bankingProject.repositories.LoanRepository;
import com.banking.bankingProject.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private LoanPaymentRepository loanPaymentRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    public Loan applyForLoan(LoanDto loanDto) {
        Customer customer = customerRepository.findByCustomerId(loanDto.getCustomerId());
        if (customer == null) {
            throw new BankServiceException("EC-105", "Customer not found", null);
        }

        Account account = accountRepository.findByAccountNumber(loanDto.getAccountNumber());
        if (account == null) {
            throw new BankServiceException("EC-106", "Account not found", null);
        }

        Loan loan = new Loan();
        loan.setLoanNumber("LOAN_" + Utility.getUuid(""));
        loan.setCustomer(customer);
        loan.setAccount(account);
        loan.setLoanType(loanDto.getLoanType());
        loan.setLoanAmount(loanDto.getLoanAmount());
        loan.setInterestRate(loanDto.getInterestRate());
        loan.setTenureMonths(loanDto.getTenureMonths());
        loan.setOutstandingAmount(loanDto.getLoanAmount());
        loan.setLoanStatus(LoanStatus.PENDING);
        loan.setRemarks(loanDto.getRemarks());

        // Calculate EMI: P * r * (1+r)^n / ((1+r)^n - 1)
        BigDecimal monthlyRate = loanDto.getInterestRate().divide(BigDecimal.valueOf(100 * 12), 10,
                RoundingMode.HALF_UP);
        BigDecimal onePlusR = BigDecimal.ONE.add(monthlyRate);
        BigDecimal numerator = loanDto.getLoanAmount().multiply(monthlyRate)
                .multiply(onePlusR.pow(loanDto.getTenureMonths()));
        BigDecimal denominator = onePlusR.pow(loanDto.getTenureMonths()).subtract(BigDecimal.ONE);
        BigDecimal emi = numerator.divide(denominator, 2, RoundingMode.HALF_UP);
        loan.setEmiAmount(emi);

        return loanRepository.save(loan);
    }

    public Loan approveLoan(String loanNumber) {
        Loan loan = loanRepository.findByLoanNumber(loanNumber);
        if (loan == null) {
            throw new BankServiceException("EC-107", "Loan not found", null);
        }
        loan.setLoanStatus(LoanStatus.APPROVED);
        loan.setApprovalDate(LocalDateTime.now());
        return loanRepository.save(loan);
    }

    public Loan rejectLoan(String loanNumber, String remarks) {
        Loan loan = loanRepository.findByLoanNumber(loanNumber);
        if (loan == null) {
            throw new BankServiceException("EC-107", "Loan not found", null);
        }
        loan.setLoanStatus(LoanStatus.REJECTED);
        loan.setRemarks(remarks);
        return loanRepository.save(loan);
    }

    public Loan disburseLoan(String loanNumber) {
        Loan loan = loanRepository.findByLoanNumber(loanNumber);
        if (loan == null) {
            throw new BankServiceException("EC-107", "Loan not found", null);
        }
        if (!LoanStatus.APPROVED.equals(loan.getLoanStatus())) {
            throw new BankServiceException("EC-108", "Loan is not approved", null);
        }

        // Credit the loan amount to the account
        Account account = loan.getAccount();
        account.setBalance(account.getBalance().add(loan.getLoanAmount()));
        accountRepository.save(account);

        loan.setLoanStatus(LoanStatus.DISBURSED);
        loan.setDisbursementDate(LocalDateTime.now());
        loan.setNextEmiDate(LocalDate.now().plusMonths(1));

        return loanRepository.save(loan);
    }

    public LoanPayment makePayment(String loanNumber, BigDecimal amount) {
        Loan loan = loanRepository.findByLoanNumber(loanNumber);
        if (loan == null) {
            throw new BankServiceException("EC-107", "Loan not found", null);
        }

        if (!LoanStatus.DISBURSED.equals(loan.getLoanStatus()) && !LoanStatus.ACTIVE.equals(loan.getLoanStatus())) {
            throw new BankServiceException("EC-109", "Loan is not active", null);
        }

        // Simple interest calculation for payment breakdown
        BigDecimal interestAmount = loan.getOutstandingAmount()
                .multiply(loan.getInterestRate())
                .divide(BigDecimal.valueOf(100 * 12), 2, RoundingMode.HALF_UP);
        BigDecimal principalAmount = amount.subtract(interestAmount);

        LoanPayment payment = new LoanPayment();
        payment.setLoan(loan);
        payment.setPaymentAmount(amount);
        payment.setPrincipalAmount(principalAmount);
        payment.setInterestAmount(interestAmount);
        payment.setPaymentReference("PAY_" + Utility.getUuid(""));

        // Update loan outstanding amount
        loan.setOutstandingAmount(loan.getOutstandingAmount().subtract(principalAmount));
        loan.setNextEmiDate(loan.getNextEmiDate().plusMonths(1));

        if (loan.getOutstandingAmount().compareTo(BigDecimal.ZERO) <= 0) {
            loan.setLoanStatus(LoanStatus.CLOSED);
        } else {
            loan.setLoanStatus(LoanStatus.ACTIVE);
        }

        loanRepository.save(loan);
        return loanPaymentRepository.save(payment);
    }

    public List<Loan> getLoansByCustomer(String customerId) {
        return loanRepository.findByCustomer_CustomerId(customerId);
    }

    public Loan getLoanDetails(String loanNumber) {
        Loan loan = loanRepository.findByLoanNumber(loanNumber);
        if (loan == null) {
            throw new BankServiceException("EC-107", "Loan not found", null);
        }
        return loan;
    }

    public List<LoanPayment> getLoanPaymentHistory(String loanNumber) {
        return loanPaymentRepository.findByLoan_LoanNumberOrderByPaymentDateDesc(loanNumber);
    }
}

package com.banking.bankingProject.services;

import com.banking.bankingProject.dto.StandingInstructionDto;
import com.banking.bankingProject.entities.Account;
import com.banking.bankingProject.entities.StandingInstruction;
import com.banking.bankingProject.enums.StandingInstructionFrequency;
import com.banking.bankingProject.enums.StandingInstructionStatus;
import com.banking.bankingProject.enums.TransactionTypeEnum;
import com.banking.bankingProject.exception.BankServiceException;
import com.banking.bankingProject.repositories.AccountRepository;
import com.banking.bankingProject.repositories.StandingInstructionRepository;
import com.banking.bankingProject.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class StandingInstructionService {

    @Autowired
    private StandingInstructionRepository standingInstructionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionService transactionService;

    public StandingInstruction createStandingInstruction(StandingInstructionDto dto) {
        Account fromAccount = accountRepository.findByAccountNumber(dto.getFromAccountNumber());
        if (fromAccount == null) {
            throw new BankServiceException("EC-110", "From account not found", null);
        }

        Account toAccount = accountRepository.findByAccountNumber(dto.getToAccountNumber());
        if (toAccount == null) {
            throw new BankServiceException("EC-111", "To account not found", null);
        }

        StandingInstruction instruction = new StandingInstruction();
        instruction.setInstructionNumber("SI_" + Utility.getUuid(""));
        instruction.setFromAccount(fromAccount);
        instruction.setToAccountNumber(dto.getToAccountNumber());
        instruction.setToBeneficiaryName(dto.getToBeneficiaryName());
        instruction.setAmount(dto.getAmount());
        instruction.setFrequency(dto.getFrequency());
        instruction.setStartDate(dto.getStartDate());
        instruction.setEndDate(dto.getEndDate());
        instruction.setNextExecutionDate(dto.getStartDate());
        instruction.setStatus(StandingInstructionStatus.ACTIVE);
        instruction.setDescription(dto.getDescription());

        return standingInstructionRepository.save(instruction);
    }

    public List<StandingInstruction> getStandingInstructions(String accountNumber) {
        return standingInstructionRepository.findByFromAccount_AccountNumber(accountNumber);
    }

    public List<StandingInstruction> getActiveStandingInstructions(String accountNumber) {
        return standingInstructionRepository.findByFromAccount_AccountNumberAndStatus(
                accountNumber, StandingInstructionStatus.ACTIVE);
    }

    public StandingInstruction pauseStandingInstruction(String instructionNumber) {
        StandingInstruction instruction = standingInstructionRepository.findByInstructionNumber(instructionNumber);
        if (instruction == null) {
            throw new BankServiceException("EC-112", "Standing instruction not found", null);
        }
        instruction.setStatus(StandingInstructionStatus.PAUSED);
        return standingInstructionRepository.save(instruction);
    }

    public StandingInstruction resumeStandingInstruction(String instructionNumber) {
        StandingInstruction instruction = standingInstructionRepository.findByInstructionNumber(instructionNumber);
        if (instruction == null) {
            throw new BankServiceException("EC-112", "Standing instruction not found", null);
        }
        instruction.setStatus(StandingInstructionStatus.ACTIVE);
        return standingInstructionRepository.save(instruction);
    }

    public StandingInstruction cancelStandingInstruction(String instructionNumber) {
        StandingInstruction instruction = standingInstructionRepository.findByInstructionNumber(instructionNumber);
        if (instruction == null) {
            throw new BankServiceException("EC-112", "Standing instruction not found", null);
        }
        instruction.setStatus(StandingInstructionStatus.CANCELLED);
        return standingInstructionRepository.save(instruction);
    }

    // Method to be called by scheduler
    public void executeDueInstructions() {
        List<StandingInstruction> dueInstructions = standingInstructionRepository.findDueInstructions(LocalDate.now());

        for (StandingInstruction instruction : dueInstructions) {
            try {
                // Execute the transfer
                String txnId = transactionService.transfer(
                        instruction.getFromAccount().getAccountNumber(),
                        instruction.getToAccountNumber(),
                        instruction.getAmount());

                // Update instruction
                instruction.setLastExecutionDate(LocalDateTime.now());
                instruction.setExecutionCount(instruction.getExecutionCount() + 1);
                instruction.setNextExecutionDate(calculateNextExecutionDate(
                        instruction.getNextExecutionDate(), instruction.getFrequency()));

                // Check if instruction should expire
                if (instruction.getEndDate() != null &&
                        instruction.getNextExecutionDate().isAfter(instruction.getEndDate())) {
                    instruction.setStatus(StandingInstructionStatus.EXPIRED);
                }

                standingInstructionRepository.save(instruction);
            } catch (Exception e) {
                instruction.setFailedCount(instruction.getFailedCount() + 1);
                standingInstructionRepository.save(instruction);
            }
        }
    }

    private LocalDate calculateNextExecutionDate(LocalDate currentDate, StandingInstructionFrequency frequency) {
        switch (frequency) {
            case DAILY:
                return currentDate.plusDays(1);
            case WEEKLY:
                return currentDate.plusWeeks(1);
            case MONTHLY:
                return currentDate.plusMonths(1);
            case QUARTERLY:
                return currentDate.plusMonths(3);
            case YEARLY:
                return currentDate.plusYears(1);
            default:
                return currentDate.plusMonths(1);
        }
    }
}

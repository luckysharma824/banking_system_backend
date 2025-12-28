package com.banking.bankingProject.repositories;

import com.banking.bankingProject.entities.StandingInstruction;
import com.banking.bankingProject.enums.StandingInstructionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StandingInstructionRepository extends JpaRepository<StandingInstruction, Long> {

    StandingInstruction findByInstructionNumber(String instructionNumber);

    List<StandingInstruction> findByFromAccount_AccountNumber(String accountNumber);

    List<StandingInstruction> findByFromAccount_AccountNumberAndStatus(String accountNumber,
            StandingInstructionStatus status);

    @Query("SELECT si FROM StandingInstruction si WHERE si.status = 'ACTIVE' AND si.nextExecutionDate <= :date")
    List<StandingInstruction> findDueInstructions(@Param("date") LocalDate date);
}

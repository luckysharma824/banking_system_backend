package com.banking.bankingProject.schedulers;

import com.banking.bankingProject.services.StandingInstructionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StandingInstructionScheduler {

    @Autowired
    private StandingInstructionService standingInstructionService;

    // Execute every day at 12:00 AM
    @Scheduled(cron = "0 0 0 * * ?")
    public void executeStandingInstructions() {
        System.out.println("Executing standing instructions...");
        standingInstructionService.executeDueInstructions();
    }
}

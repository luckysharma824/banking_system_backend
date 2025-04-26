package com.banking.bankingProject.schedulers;

import com.banking.bankingProject.enums.AccountStatus;
import com.banking.bankingProject.repositories.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AccountSchedulerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountSchedulerService.class);

    private final AccountRepository accountRepository;

    public AccountSchedulerService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Scheduled(cron = "0 0 0 * * *") // per minute cron = 0 * * * * *
    public void updateAccountStatus() {
        LOGGER.info("Account inactive job started...");
        LocalDateTime dateTime = LocalDateTime.now().minusMonths(12);
        int count = accountRepository.updateAccountStatusByLastTransactionDate(AccountStatus.INACTIVE, dateTime);
        LOGGER.info("Account inactive job completed, count: {}", count);
    }

}

package com.banking.bankingProject.util;

import com.banking.bankingProject.entities.EndpointSecurity;
import com.banking.bankingProject.enums.RoleEnum;
import com.banking.bankingProject.repositories.EndpointSecurityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Initializes endpoint security rules on application startup if database is
 * empty
 */
//@Component
public class EndpointSecurityInitializer implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(EndpointSecurityInitializer.class);

    private final EndpointSecurityRepository endpointSecurityRepository;

    public EndpointSecurityInitializer(EndpointSecurityRepository endpointSecurityRepository) {
        this.endpointSecurityRepository = endpointSecurityRepository;
    }

    @Override
    public void run(String... args) {
        if (endpointSecurityRepository.count() == 0) {
            LOGGER.info("Initializing endpoint security rules...");
            initializeSecurityRules();
            LOGGER.info("Endpoint security rules initialized successfully");
        } else {
            LOGGER.info("Endpoint security rules already exist, skipping initialization");
        }
    }

    private void initializeSecurityRules() {
        // Public endpoints
        createRule("/auth/login", null, Set.of(), 1, true);
        createRule("/users/roles", null, Set.of(), 2, true);
        createRule("/**", "OPTIONS", Set.of(), 3, true);

        // Endpoint Security management (ADMIN only)
        createRule("/endpoint-security/**", null, Set.of(RoleEnum.ROLE_ADMIN), 5, false);

        // User management endpoints (ADMIN only)
        createRule("/users", null, Set.of(RoleEnum.ROLE_ADMIN), 10, false);
        createRule("/users/create", null, Set.of(RoleEnum.ROLE_ADMIN), 11, false);

        // Customer create (ADMIN, CLERK, MANAGER)
        createRule("/customers/create", null,
                Set.of(RoleEnum.ROLE_ADMIN, RoleEnum.ROLE_CLERK, RoleEnum.ROLE_MANAGER), 20, false);

        // Customer search (ADMIN, CLERK, MANAGER, CASHIER)
        createRule("/customers/search", null,
                Set.of(RoleEnum.ROLE_ADMIN, RoleEnum.ROLE_CLERK, RoleEnum.ROLE_MANAGER, RoleEnum.ROLE_CASHIER),
                21, false);

        // Account balance (All roles)
        createRule("/accounts/balance/**", null,
                Set.of(RoleEnum.ROLE_ADMIN, RoleEnum.ROLE_CLERK, RoleEnum.ROLE_MANAGER, RoleEnum.ROLE_CASHIER),
                30, false);

        // Accounts GET (CASHIER can view)
        createRule("/accounts/**", "GET",
                Set.of(RoleEnum.ROLE_ADMIN, RoleEnum.ROLE_CLERK, RoleEnum.ROLE_MANAGER, RoleEnum.ROLE_CASHIER),
                31, false);

        // Accounts all methods (ADMIN, CLERK, MANAGER)
        createRule("/accounts/**", null,
                Set.of(RoleEnum.ROLE_ADMIN, RoleEnum.ROLE_CLERK, RoleEnum.ROLE_MANAGER),
                32, false);

        // Transactions (ADMIN, CASHIER)
        createRule("/transactions/**", null,
                Set.of(RoleEnum.ROLE_ADMIN, RoleEnum.ROLE_CASHIER),
                40, false);
    }

    private void createRule(String urlPattern, String httpMethod, Set<RoleEnum> roles, int priority,
            boolean permitAll) {
        EndpointSecurity rule = new EndpointSecurity();
        rule.setUrlPattern(urlPattern);
        rule.setHttpMethod(httpMethod);
        rule.setAllowedRoles(roles);
        rule.setPriority(priority);
        rule.setPermitAll(permitAll);
        rule.setEnabled(true);
        endpointSecurityRepository.save(rule);
        LOGGER.debug("Created rule: {} {} - Priority: {}, PermitAll: {}, Roles: {}",
                httpMethod != null ? httpMethod : "ALL", urlPattern, priority, permitAll, roles);
    }
}

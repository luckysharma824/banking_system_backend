package com.banking.bankingProject.util;

import com.banking.bankingProject.entities.EndpointSecurity;
import com.banking.bankingProject.enums.RoleEnum;
import com.banking.bankingProject.services.EndpointSecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Example utility class showing how to programmatically manage endpoint
 * security rules.
 * This can be useful for migrations, testing, or administrative tools.
 */
//@Component
public class EndpointSecurityExamples {

    private static final Logger LOGGER = LoggerFactory.getLogger(EndpointSecurityExamples.class);

    private final EndpointSecurityService endpointSecurityService;

    public EndpointSecurityExamples(EndpointSecurityService endpointSecurityService) {
        this.endpointSecurityService = endpointSecurityService;
    }

    /**
     * Example 1: Add a new public endpoint
     */
    public void addPublicEndpoint(String urlPattern) {
        EndpointSecurity rule = new EndpointSecurity();
        rule.setUrlPattern(urlPattern);
        rule.setHttpMethod(null); // null = all methods
        rule.setAllowedRoles(Set.of()); // empty = no roles needed
        rule.setPriority(10);
        rule.setPermitAll(true);
        rule.setEnabled(true);

        endpointSecurityService.createEndpointSecurity(rule);
        LOGGER.info("Added public endpoint: {}", urlPattern);
    }

    /**
     * Example 2: Add admin-only endpoint
     */
    public void addAdminEndpoint(String urlPattern) {
        EndpointSecurity rule = new EndpointSecurity();
        rule.setUrlPattern(urlPattern);
        rule.setHttpMethod(null);
        rule.setAllowedRoles(Set.of(RoleEnum.ROLE_ADMIN));
        rule.setPriority(20);
        rule.setPermitAll(false);
        rule.setEnabled(true);

        endpointSecurityService.createEndpointSecurity(rule);
        LOGGER.info("Added admin-only endpoint: {}", urlPattern);
    }

    /**
     * Example 3: Add endpoint with multiple roles
     */
    public void addMultiRoleEndpoint(String urlPattern, RoleEnum... roles) {
        EndpointSecurity rule = new EndpointSecurity();
        rule.setUrlPattern(urlPattern);
        rule.setHttpMethod(null);
        rule.setAllowedRoles(Set.of(roles));
        rule.setPriority(30);
        rule.setPermitAll(false);
        rule.setEnabled(true);

        endpointSecurityService.createEndpointSecurity(rule);
        LOGGER.info("Added multi-role endpoint: {} - Roles: {}", urlPattern, roles);
    }

    /**
     * Example 4: Add method-specific endpoint (e.g., only GET)
     */
    public void addReadOnlyEndpoint(String urlPattern, RoleEnum... roles) {
        EndpointSecurity rule = new EndpointSecurity();
        rule.setUrlPattern(urlPattern);
        rule.setHttpMethod("GET");
        rule.setAllowedRoles(Set.of(roles));
        rule.setPriority(40);
        rule.setPermitAll(false);
        rule.setEnabled(true);

        endpointSecurityService.createEndpointSecurity(rule);
        LOGGER.info("Added read-only endpoint: {} GET - Roles: {}", urlPattern, roles);
    }

    /**
     * Example 5: Temporarily disable an endpoint
     */
    public void disableEndpoint(Long ruleId) {
        var rules = endpointSecurityService.getAllEndpointSecurityRules();
        var rule = rules.stream()
                .filter(r -> r.getId().equals(ruleId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Rule not found: " + ruleId));

        rule.setEnabled(false);
        endpointSecurityService.updateEndpointSecurity(ruleId, rule);
        LOGGER.info("Disabled endpoint rule: {} {}", rule.getHttpMethod(), rule.getUrlPattern());
    }

    /**
     * Example 6: Change allowed roles for an endpoint
     */
    public void updateEndpointRoles(String urlPattern, RoleEnum... newRoles) {
        var rules = endpointSecurityService.getAllEndpointSecurityRules();
        var rule = rules.stream()
                .filter(r -> r.getUrlPattern().equals(urlPattern))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Rule not found: " + urlPattern));

        rule.setAllowedRoles(Set.of(newRoles));
        endpointSecurityService.updateEndpointSecurity(rule.getId(), rule);
        LOGGER.info("Updated roles for {}: {}", urlPattern, newRoles);
    }

    /**
     * Example 7: List all active security rules
     */
    public void listActiveRules() {
        var rules = endpointSecurityService.getEnabledEndpointSecurityRules();
        LOGGER.info("Active security rules ({}):", rules.size());
        rules.forEach(rule -> LOGGER.info("  [{}] {} {} - Roles: {}, Public: {}",
                rule.getPriority(),
                rule.getHttpMethod() != null ? rule.getHttpMethod() : "ALL",
                rule.getUrlPattern(),
                rule.getAllowedRoles(),
                rule.getPermitAll()));
    }

    /**
     * Example 8: Bulk import rules from a configuration
     */
    public void importRules() {
        // Example: Import a set of report endpoints
        addMultiRoleEndpoint("/reports/sales/**", RoleEnum.ROLE_ADMIN, RoleEnum.ROLE_MANAGER);
        addMultiRoleEndpoint("/reports/inventory/**", RoleEnum.ROLE_ADMIN, RoleEnum.ROLE_CLERK);
        addAdminEndpoint("/reports/audit/**");

        LOGGER.info("Bulk import completed");
    }

    /**
     * Example 9: Create a rule hierarchy (general to specific)
     */
    public void createRuleHierarchy(String basePattern) {
        // More specific rules should have LOWER priority numbers

        // Specific: POST operations (priority 10)
        EndpointSecurity createRule = new EndpointSecurity();
        createRule.setUrlPattern(basePattern + "/**");
        createRule.setHttpMethod("POST");
        createRule.setAllowedRoles(Set.of(RoleEnum.ROLE_ADMIN));
        createRule.setPriority(10);
        createRule.setPermitAll(false);
        createRule.setEnabled(true);
        endpointSecurityService.createEndpointSecurity(createRule);

        // General: GET operations (priority 20)
        EndpointSecurity readRule = new EndpointSecurity();
        readRule.setUrlPattern(basePattern + "/**");
        readRule.setHttpMethod("GET");
        readRule.setAllowedRoles(Set.of(RoleEnum.ROLE_ADMIN, RoleEnum.ROLE_CLERK, RoleEnum.ROLE_MANAGER));
        readRule.setPriority(20);
        readRule.setPermitAll(false);
        readRule.setEnabled(true);
        endpointSecurityService.createEndpointSecurity(readRule);

        LOGGER.info("Created rule hierarchy for {}", basePattern);
    }

    /**
     * Example 10: Migration - Convert hardcoded rule to database
     * 
     * Before (hardcoded in WebSecurityConfig):
     * .requestMatchers("/api/xyz/**").hasAnyRole("ADMIN", "MANAGER")
     * 
     * After (in database):
     */
    public void migrateHardcodedRule() {
        EndpointSecurity rule = new EndpointSecurity();
        rule.setUrlPattern("/api/xyz/**");
        rule.setHttpMethod(null); // all methods
        rule.setAllowedRoles(Set.of(RoleEnum.ROLE_ADMIN, RoleEnum.ROLE_MANAGER));
        rule.setPriority(50); // adjust based on your priority scheme
        rule.setPermitAll(false);
        rule.setEnabled(true);

        endpointSecurityService.createEndpointSecurity(rule);
        LOGGER.info("Migrated hardcoded rule to database: /api/xyz/**");
    }
}

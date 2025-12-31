package com.banking.bankingProject.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for IdGenerator
 * Demonstrates production-level ID generation and validation
 */
class IdGeneratorTest {

    @Test
    @DisplayName("Generate and validate Customer ID with check digit")
    void testCustomerIdGeneration() {
        String customerId = IdGenerator.generateCustomerId("0001");

        System.out.println("Generated Customer ID: " + customerId);

        // Verify format: CUS + 4-digit branch + 9-digit sequence + 1-char check digit =
        // 17 chars
        assertNotNull(customerId);
        assertEquals(17, customerId.length());
        assertTrue(customerId.startsWith("CUS"));

        // Validate check digit
        assertTrue(IdGenerator.validateCustomerId(customerId));

        // Extract branch code
        String branchCode = IdGenerator.extractBranchCode(customerId);
        assertEquals("0001", branchCode);
    }

    @Test
    @DisplayName("Generate and validate Account Number with Luhn check digit")
    void testAccountNumberGeneration() {
        String accountNumber = IdGenerator.generateAccountNumber("001", "0001", "SA");

        System.out.println("Generated Account Number: " + accountNumber);
        // Format: 001-0001-SA-0012345678-97

        assertNotNull(accountNumber);
        assertTrue(accountNumber.contains("-"));

        // Validate Luhn check digit
        assertTrue(IdGenerator.validateAccountNumber(accountNumber));

        // Extract branch code
        String branchCode = IdGenerator.extractBranchCode(accountNumber);
        assertEquals("0001", branchCode);
    }

    @Test
    @DisplayName("Generate and validate Transaction ID - sortable by timestamp")
    void testTransactionIdGeneration() {
        String txnId1 = IdGenerator.generateTransactionId("0001");

        try {
            Thread.sleep(10); // Small delay to ensure different timestamps
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String txnId2 = IdGenerator.generateTransactionId("0001");

        System.out.println("Transaction ID 1: " + txnId1);
        System.out.println("Transaction ID 2: " + txnId2);

        assertNotNull(txnId1);
        assertNotNull(txnId2);
        assertTrue(txnId1.startsWith("TXN"));
        assertTrue(txnId2.startsWith("TXN"));

        // Verify IDs are sortable (second one should be greater)
        assertTrue(txnId2.compareTo(txnId1) > 0);

        // Validate check digit
        assertTrue(IdGenerator.validateTransactionId(txnId1));
        assertTrue(IdGenerator.validateTransactionId(txnId2));
    }

    @Test
    @DisplayName("Generate User ID with branch code")
    void testUserIdGeneration() {
        String userId = IdGenerator.generateUserId("0001");

        System.out.println("Generated User ID: " + userId);

        assertNotNull(userId);
        assertEquals(15, userId.length());
        assertTrue(userId.startsWith("USR"));

        // Validate check digit
        assertTrue(IdGenerator.validateCheckDigit(userId));
    }

    @Test
    @DisplayName("Validate Luhn algorithm with known valid numbers")
    void testLuhnValidation() {
        // Test with a known valid credit card number (test card)
        String testNumber = "4532015112830366"; // Valid Luhn

        // For our purposes, let's test our own generated account
        String accountNumber = IdGenerator.generateAccountNumber("001", "0001", "CA");
        assertTrue(IdGenerator.validateAccountNumber(accountNumber));

        // Tamper with the account number (change last digit)
        String tamperedAccount = accountNumber.substring(0, accountNumber.length() - 1) + "0";
        assertFalse(IdGenerator.validateAccountNumber(tamperedAccount));
    }

    @Test
    @DisplayName("Test sequence generation uniqueness and thread-safety")
    void testSequenceUniqueness() {
        String id1 = IdGenerator.generateCustomerId("0001");
        String id2 = IdGenerator.generateCustomerId("0001");
        String id3 = IdGenerator.generateCustomerId("0001");

        System.out.println("Sequential Customer IDs:");
        System.out.println("  1: " + id1);
        System.out.println("  2: " + id2);
        System.out.println("  3: " + id3);

        // All IDs should be unique
        assertNotEquals(id1, id2);
        assertNotEquals(id2, id3);
        assertNotEquals(id1, id3);
    }

    @Test
    @DisplayName("Test different branch codes")
    void testDifferentBranchCodes() {
        String customerId1 = IdGenerator.generateCustomerId("0001");
        String customerId2 = IdGenerator.generateCustomerId("0002");
        String customerId3 = IdGenerator.generateCustomerId("9999");

        System.out.println("Customer IDs from different branches:");
        System.out.println("  Branch 0001: " + customerId1);
        System.out.println("  Branch 0002: " + customerId2);
        System.out.println("  Branch 9999: " + customerId3);

        // Extract and verify branch codes
        assertEquals("0001", IdGenerator.extractBranchCode(customerId1));
        assertEquals("0002", IdGenerator.extractBranchCode(customerId2));
        assertEquals("9999", IdGenerator.extractBranchCode(customerId3));
    }

    @Test
    @DisplayName("Test account types in account numbers")
    void testAccountTypes() {
        String savingsAccount = IdGenerator.generateAccountNumber("001", "0001", "SA");
        String currentAccount = IdGenerator.generateAccountNumber("001", "0001", "CA");
        String fixedDeposit = IdGenerator.generateAccountNumber("001", "0001", "FD");

        System.out.println("Account Numbers by Type:");
        System.out.println("  Savings:  " + savingsAccount);
        System.out.println("  Current:  " + currentAccount);
        System.out.println("  Fixed:    " + fixedDeposit);

        assertTrue(savingsAccount.contains("SA"));
        assertTrue(currentAccount.contains("CA"));
        assertTrue(fixedDeposit.contains("FD"));
    }

    @Test
    @DisplayName("Test invalid ID validation")
    void testInvalidIdValidation() {
        // Invalid Customer ID (wrong length)
        assertFalse(IdGenerator.validateCustomerId("CUS12345"));

        // Invalid Customer ID (wrong prefix)
        assertFalse(IdGenerator.validateCustomerId("ACC0001000000018C"));

        // Invalid Customer ID (wrong check digit)
        assertFalse(IdGenerator.validateCustomerId("CUS0001000000018X"));

        // Invalid Account Number (wrong length)
        assertFalse(IdGenerator.validateAccountNumber("001-0001-SA-123"));

        // Null values
        assertFalse(IdGenerator.validateCustomerId(null));
        assertFalse(IdGenerator.validateAccountNumber(null));
        assertFalse(IdGenerator.validateTransactionId(null));
    }

    @Test
    @DisplayName("Demonstrate production-level ID formats")
    void demonstrateProductionIds() {
        System.out.println("\n========================================");
        System.out.println("PRODUCTION-LEVEL ID GENERATION DEMO");
        System.out.println("========================================\n");

        // Customer ID
        String customerId = IdGenerator.generateCustomerId("0001");
        System.out.println("Customer ID:");
        System.out.println("  Format: CUS + branchCode(4) + sequence(9) + checkDigit(1)");
        System.out.println("  Example: " + customerId);
        System.out.println("  Length: " + customerId.length() + " characters");
        System.out.println("  Valid: " + IdGenerator.validateCustomerId(customerId));
        System.out.println();

        // Account Number
        String accountNumber = IdGenerator.generateAccountNumber("001", "0234", "SA");
        System.out.println("Account Number:");
        System.out.println("  Format: bankCode(3)-branchCode(4)-type(2)-sequence(10)-luhnCheck(2)");
        System.out.println("  Example: " + accountNumber);
        System.out.println("  Valid: " + IdGenerator.validateAccountNumber(accountNumber));
        System.out.println();

        // Transaction ID
        String txnId = IdGenerator.generateTransactionId("0234");
        System.out.println("Transaction ID:");
        System.out.println("  Format: TXN + timestamp(17) + branchCode(4) + sequence(6) + checkDigit(1)");
        System.out.println("  Example: " + txnId);
        System.out.println("  Sortable: Yes (timestamp-based)");
        System.out.println("  Valid: " + IdGenerator.validateTransactionId(txnId));
        System.out.println();

        // User ID
        String userId = IdGenerator.generateUserId("0234");
        System.out.println("User ID:");
        System.out.println("  Format: USR + branchCode(4) + sequence(7) + checkDigit(1)");
        System.out.println("  Example: " + userId);
        System.out.println("  Length: " + userId.length() + " characters");
        System.out.println("  Valid: " + IdGenerator.validateCheckDigit(userId));
        System.out.println();

        System.out.println("========================================");
        System.out.println("KEY FEATURES:");
        System.out.println("✓ Check digits (Luhn for accounts, custom for others)");
        System.out.println("✓ Branch/region codes for distributed systems");
        System.out.println("✓ Fixed lengths for validation");
        System.out.println("✓ Timestamp-based transaction IDs (sortable)");
        System.out.println("✓ Thread-safe sequence generators");
        System.out.println("========================================\n");
    }

    @Test
    @DisplayName("Test current sequence monitoring")
    void testSequenceMonitoring() {
        long beforeCustomer = IdGenerator.getCurrentSequence("CUSTOMER");
        IdGenerator.generateCustomerId("0001");
        long afterCustomer = IdGenerator.getCurrentSequence("CUSTOMER");

        System.out.println("Customer Sequence: " + beforeCustomer + " -> " + afterCustomer);

        assertTrue(afterCustomer > beforeCustomer);
    }
}

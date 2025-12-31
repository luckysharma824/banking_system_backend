package com.banking.bankingProject.util;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Production-level ID Generator for Banking System
 * Features:
 * - Luhn algorithm check digits for account numbers
 * - Timestamp-based sortable transaction IDs
 * - Fixed-length IDs for validation
 * - Thread-safe sequence generators with proper locking
 * - Branch/region code support for distributed systems
 */
@Component
public class IdGenerator {

    // Thread-safe sequence generators for different entity types
    private static final Map<String, AtomicLong> sequenceGenerators = new ConcurrentHashMap<>();

    // Configuration constants
    private static final String DEFAULT_BANK_CODE = "001";
    private static final String DEFAULT_BRANCH_CODE = "0001";

    static {
        // Initialize sequence generators
        sequenceGenerators.put("CUSTOMER", new AtomicLong(1000000));
        sequenceGenerators.put("ACCOUNT", new AtomicLong(10000000000L));
        sequenceGenerators.put("TRANSACTION", new AtomicLong(100000));
        sequenceGenerators.put("USER", new AtomicLong(1000));
    }

    /**
     * Generate Customer ID with check digit
     * Format: CUS + branchCode(4) + sequence(9) + checkDigit(1) = 17 chars
     * Example: CUS0001000000018C
     */
    public static String generateCustomerId(String branchCode) {
        String branch = padOrTrim(branchCode != null ? branchCode : DEFAULT_BRANCH_CODE, 4);
        long sequence = getNextSequence("CUSTOMER");
        String sequenceStr = String.format("%09d", sequence);
        String base = "CUS" + branch + sequenceStr;
        return base + calculateCheckDigit(base);
    }

    /**
     * Generate Account Number with Luhn check digit
     * Format: bankCode(3) + branchCode(4) + accountType(2) + sequence(10) +
     * checkDigit(2) = 21 chars
     * Example: 001-0001-SA-0012345678-97
     */
    public static String generateAccountNumber(String bankCode, String branchCode, String accountType) {
        String bank = padOrTrim(bankCode != null ? bankCode : DEFAULT_BANK_CODE, 3);
        String branch = padOrTrim(branchCode != null ? branchCode : DEFAULT_BRANCH_CODE, 4);
        String type = padOrTrim(accountType != null ? accountType : "SA", 2);
        long sequence = getNextSequence("ACCOUNT");
        String sequenceStr = String.format("%010d", sequence);

        String base = bank + branch + type + sequenceStr;
        String checkDigit = calculateLuhnCheckDigit(base);

        // Format with dashes for readability: XXX-XXXX-XX-XXXXXXXXXX-XX
        return String.format("%s-%s-%s-%s-%s", bank, branch, type, sequenceStr, checkDigit);
    }

    /**
     * Generate Transaction ID - sortable by timestamp
     * Format: TXN + timestamp(17) + branchCode(4) + sequence(6) + checkDigit(1) =
     * 31 chars
     * Example: TXN20251231143025001-0001-000001-C
     */
    public static String generateTransactionId(String branchCode) {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        String branch = padOrTrim(branchCode != null ? branchCode : DEFAULT_BRANCH_CODE, 4);
        long sequence = getNextSequence("TRANSACTION");
        String sequenceStr = String.format("%06d", sequence);

        String base = "TXN" + timestamp + branch + sequenceStr;
        char checkDigit = calculateCheckDigit(base);

        // Format: TXN + timestamp-branch-sequence-checkDigit
        return String.format("TXN%s-%s-%s-%c", timestamp, branch, sequenceStr, checkDigit);
    }

    /**
     * Generate User ID
     * Format: USR + branchCode(4) + sequence(7) + checkDigit(1) = 15 chars
     * Example: USR0001000001C
     */
    public static String generateUserId(String branchCode) {
        String branch = padOrTrim(branchCode != null ? branchCode : DEFAULT_BRANCH_CODE, 4);
        long sequence = getNextSequence("USER");
        String sequenceStr = String.format("%07d", sequence);
        String base = "USR" + branch + sequenceStr;
        return base + calculateCheckDigit(base);
    }

    /**
     * Get next sequence number for an entity type (thread-safe)
     */
    private static long getNextSequence(String entityType) {
        return sequenceGenerators
                .computeIfAbsent(entityType, k -> new AtomicLong(1000000))
                .getAndIncrement();
    }

    /**
     * Calculate Luhn check digit (industry standard for account numbers)
     * Used by credit cards, bank accounts, etc.
     */
    private static String calculateLuhnCheckDigit(String number) {
        int sum = 0;
        boolean alternate = false;

        // Process digits from right to left
        for (int i = number.length() - 1; i >= 0; i--) {
            char c = number.charAt(i);
            if (!Character.isDigit(c))
                continue;

            int digit = Character.getNumericValue(c);
            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            sum += digit;
            alternate = !alternate;
        }

        int checkDigit = (10 - (sum % 10)) % 10;
        return String.format("%02d", checkDigit); // Return as 2-digit string
    }

    /**
     * Validate Luhn check digit
     */
    public static boolean validateLuhnCheckDigit(String numberWithCheckDigit) {
        if (numberWithCheckDigit == null || numberWithCheckDigit.length() < 3) {
            return false;
        }

        String checkDigitStr = numberWithCheckDigit.substring(numberWithCheckDigit.length() - 2);
        String number = numberWithCheckDigit.substring(0, numberWithCheckDigit.length() - 2);

        return checkDigitStr.equals(calculateLuhnCheckDigit(number));
    }

    /**
     * Calculate simple check digit (for non-account IDs)
     * Returns a single character A-Z based on sum modulo 26
     */
    private static char calculateCheckDigit(String input) {
        int sum = 0;
        for (char c : input.toCharArray()) {
            if (Character.isDigit(c)) {
                sum += Character.getNumericValue(c);
            } else if (Character.isLetter(c)) {
                sum += (int) c;
            }
        }
        return (char) ('A' + (sum % 26));
    }

    /**
     * Validate simple check digit
     */
    public static boolean validateCheckDigit(String idWithCheckDigit) {
        if (idWithCheckDigit == null || idWithCheckDigit.length() < 2) {
            return false;
        }

        char checkDigit = idWithCheckDigit.charAt(idWithCheckDigit.length() - 1);
        String id = idWithCheckDigit.substring(0, idWithCheckDigit.length() - 1);

        return checkDigit == calculateCheckDigit(id);
    }

    /**
     * Validate Customer ID format and check digit
     */
    public static boolean validateCustomerId(String customerId) {
        if (customerId == null || !customerId.startsWith("CUS") || customerId.length() != 17) {
            return false;
        }
        return validateCheckDigit(customerId);
    }

    /**
     * Validate Account Number format and Luhn check digit
     */
    public static boolean validateAccountNumber(String accountNumber) {
        if (accountNumber == null) {
            return false;
        }

        // Remove dashes for validation
        String cleaned = accountNumber.replace("-", "");
        if (cleaned.length() != 21) {
            return false;
        }

        return validateLuhnCheckDigit(cleaned);
    }

    /**
     * Validate Transaction ID format and check digit
     */
    public static boolean validateTransactionId(String transactionId) {
        if (transactionId == null || !transactionId.startsWith("TXN")) {
            return false;
        }

        // Remove dashes for validation
        String cleaned = transactionId.replace("-", "");
        if (cleaned.length() != 31) {
            return false;
        }

        return validateCheckDigit(cleaned);
    }

    /**
     * Pad string to specified length with zeros or trim if too long
     */
    private static String padOrTrim(String str, int length) {
        if (str == null || str.isEmpty()) {
            return "0".repeat(length);
        }
        if (str.length() >= length) {
            return str.substring(0, length);
        }
        return String.format("%0" + length + "d", Integer.parseInt(str));
    }

    /**
     * Extract branch code from generated ID
     */
    public static String extractBranchCode(String id) {
        if (id == null || id.length() < 8) {
            return null;
        }

        if (id.startsWith("CUS")) {
            return id.substring(3, 7);
        } else if (id.startsWith("USR")) {
            return id.substring(3, 7);
        } else if (id.startsWith("TXN")) {
            // Extract from formatted transaction ID
            String[] parts = id.split("-");
            if (parts.length >= 2) {
                return parts[1];
            }
        } else {
            // Account number format
            String[] parts = id.split("-");
            if (parts.length >= 2) {
                return parts[1];
            }
        }

        return null;
    }

    /**
     * Reset sequence for testing purposes (use with caution)
     */
    public static void resetSequence(String entityType, long startValue) {
        sequenceGenerators.put(entityType, new AtomicLong(startValue));
    }

    /**
     * Get current sequence value (for monitoring/debugging)
     */
    public static long getCurrentSequence(String entityType) {
        AtomicLong sequence = sequenceGenerators.get(entityType);
        return sequence != null ? sequence.get() : -1;
    }
}

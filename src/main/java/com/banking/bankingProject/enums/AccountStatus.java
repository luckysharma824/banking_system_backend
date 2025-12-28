package com.banking.bankingProject.enums;

public enum AccountStatus {
    ACTIVE,
    INACTIVE,
    FROZEN,
    CLOSED;

    public static AccountStatus getAccountStatus(String value) {
        for (AccountStatus status : AccountStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid account status: " + value);
    }
}

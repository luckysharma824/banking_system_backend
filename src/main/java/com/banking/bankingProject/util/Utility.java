package com.banking.bankingProject.util;

import java.util.UUID;

public class Utility {

    private Utility() {}

    public static String getUuid(String replacement) {
        return UUID.randomUUID().toString().replace("-", replacement);
    }
}

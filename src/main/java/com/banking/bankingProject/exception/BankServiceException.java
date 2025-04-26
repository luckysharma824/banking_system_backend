package com.banking.bankingProject.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BankServiceException extends RuntimeException {
    private String errorCode;
    private String errorMessage;
    private Exception exception;

    public BankServiceException(String errorCode, String errorMessage, Exception exception) {
        super(errorCode + ":" + errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.exception = exception;
    }
}

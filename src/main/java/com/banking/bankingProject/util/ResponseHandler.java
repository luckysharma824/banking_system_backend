package com.banking.bankingProject.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResponseHandler {

    private ResponseHandler() {
    }

    public static ResponseEntity<Object> handle(Object data, String message, boolean isSuccess, HttpStatus httpStatus) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("data", data);
        response.put("message", message);
        response.put("isSuccess", isSuccess);
        response.put("timeStamp", Instant.now().getEpochSecond());
        return ResponseEntity.status(httpStatus).body(response);
    }

    public static ResponseEntity<Object> handle(String message, boolean isSuccess, HttpStatus httpStatus) {
        return handle(null, message, isSuccess, httpStatus);
    }

}

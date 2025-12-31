package com.banking.bankingProject.exception;

import com.banking.bankingProject.util.ResponseHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handle business logic exceptions
     */
    @ExceptionHandler(BankServiceException.class)
    public ResponseEntity<Object> handleBankServiceException(
            BankServiceException ex, HttpServletRequest request) {
        LOGGER.error("Business exception - Code: {}, Message: {}", ex.getErrorCode(), ex.getErrorMessage(), ex);

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("error", "Business Logic Error");
        errorResponse.put("errorCode", ex.getErrorCode());
        errorResponse.put("message", ex.getErrorMessage());
        errorResponse.put("path", request.getRequestURI());
        return ResponseHandler.handle(errorResponse, ex.getErrorMessage(), false, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle validation errors from @Valid annotations
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        LOGGER.error("Validation error: {}", ex.getMessage());

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("error", "Validation Failed");
        errorResponse.put("message", "Input validation failed");
        errorResponse.put("fieldErrors", fieldErrors);
        errorResponse.put("path", request.getRequestURI());
        return ResponseHandler.handle(errorResponse, "Validation failed", false, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle authentication exceptions (401)
     */
    @ExceptionHandler({AuthenticationException.class, BadCredentialsException.class,
            InsufficientAuthenticationException.class})
    public ResponseEntity<Object> handleAuthenticationException(Exception ex, HttpServletRequest request) {
        LOGGER.error("Authentication error: {}", ex.getMessage());

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", HttpStatus.UNAUTHORIZED.value());
        errorResponse.put("error", "Unauthorized");
        errorResponse.put("message", ex.getMessage() != null ? ex.getMessage() : "Authentication failed");
        errorResponse.put("path", request.getRequestURI());

        return ResponseHandler.handle(errorResponse, ex.getMessage(), false, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handle authorization exceptions (403)
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(
            AccessDeniedException ex, HttpServletRequest request) {
        LOGGER.error("Access denied: {}", ex.getMessage());

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", HttpStatus.FORBIDDEN.value());
        errorResponse.put("error", "Forbidden");
        errorResponse.put("message", "You don't have permission to access this resource");
        errorResponse.put("path", request.getRequestURI());

        return ResponseHandler.handle(errorResponse, "Access denied", false, HttpStatus.FORBIDDEN);
    }

    /**
     * Handle illegal argument exceptions
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(
            IllegalArgumentException ex, HttpServletRequest request) {
        LOGGER.error("Illegal argument: {}", ex.getMessage(), ex);

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("error", "Bad Request");
        errorResponse.put("message", ex.getMessage());
        errorResponse.put("path", request.getRequestURI());

        return ResponseHandler.handle(errorResponse, ex.getMessage(), false, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle method argument type mismatch
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleTypeMismatchException(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        LOGGER.error("Type mismatch error: {}", ex.getMessage());

        String message = String.format("Parameter '%s' should be of type %s",
                ex.getName(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown");

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("error", "Bad Request");
        errorResponse.put("message", message);
        errorResponse.put("path", request.getRequestURI());

        return ResponseHandler.handle(errorResponse, message, false, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle null pointer exceptions
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> handleNullPointerException(
            NullPointerException ex, HttpServletRequest request) {
        LOGGER.error("Null pointer exception at: {}", request.getRequestURI(), ex);

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.put("error", "Internal Server Error");
        errorResponse.put("message", "An unexpected error occurred. Please contact support.");
        errorResponse.put("path", request.getRequestURI());

        return ResponseHandler.handle(errorResponse, "Null pointer exception occurred", false,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handle all other unexpected exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(
            Exception ex, HttpServletRequest request) {
        LOGGER.error("Unexpected error at: {}", request.getRequestURI(), ex);

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.put("error", "Internal Server Error");
        errorResponse.put("message", "An unexpected error occurred: " + ex.getMessage());
        errorResponse.put("path", request.getRequestURI());

        return ResponseHandler.handle(errorResponse, "Unexpected error occurred", false,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

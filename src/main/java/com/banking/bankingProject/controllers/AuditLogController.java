package com.banking.bankingProject.controllers;

import com.banking.bankingProject.entities.AuditLog;
import com.banking.bankingProject.services.AuditLogService;
import com.banking.bankingProject.util.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/audit-logs")
public class AuditLogController {

    @Autowired
    private AuditLogService auditLogService;

    @GetMapping("/entity/{entityType}/{entityId}")
    public ResponseEntity<Object> getAuditLogsByEntity(
            @PathVariable String entityType,
            @PathVariable Long entityId) {
        List<AuditLog> logs = auditLogService.getAuditLogsByEntity(entityType, entityId);
        return ResponseHandler.handle(logs, "Audit logs retrieved successfully", true, HttpStatus.OK);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<Object> getAuditLogsByUser(@PathVariable String username) {
        List<AuditLog> logs = auditLogService.getAuditLogsByUser(username);
        return ResponseHandler.handle(logs, "Audit logs retrieved successfully", true, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllAuditLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<AuditLog> logs = auditLogService.getAllAuditLogs(page, size);
        return ResponseHandler.handle(logs, "Audit logs retrieved successfully", true, HttpStatus.OK);
    }

    @GetMapping("/dateRange")
    public ResponseEntity<Object> getAuditLogsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<AuditLog> logs = auditLogService.getAuditLogsByDateRange(startDate, endDate);
        return ResponseHandler.handle(logs, "Audit logs retrieved successfully", true, HttpStatus.OK);
    }

    @GetMapping("/entityType/{entityType}")
    public ResponseEntity<Object> getAuditLogsByEntityType(@PathVariable String entityType) {
        List<AuditLog> logs = auditLogService.getAuditLogsByEntityType(entityType);
        return ResponseHandler.handle(logs, "Audit logs retrieved successfully", true, HttpStatus.OK);
    }
}

package com.banking.bankingProject.services;

import com.banking.bankingProject.entities.AuditLog;
import com.banking.bankingProject.repositories.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditLogService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    public AuditLog createAuditLog(String entityType, Long entityId, String action,
            String performedBy, String ipAddress,
            String oldValue, String newValue, String description) {
        AuditLog auditLog = new AuditLog();
        auditLog.setEntityType(entityType);
        auditLog.setEntityId(entityId);
        auditLog.setAction(action);
        auditLog.setPerformedBy(performedBy);
        auditLog.setIpAddress(ipAddress);
        auditLog.setOldValue(oldValue);
        auditLog.setNewValue(newValue);
        auditLog.setDescription(description);
        return auditLogRepository.save(auditLog);
    }

    public List<AuditLog> getAuditLogsByEntity(String entityType, Long entityId) {
        return auditLogRepository.findByEntityTypeAndEntityIdOrderByTimestampDesc(entityType, entityId);
    }

    public List<AuditLog> getAuditLogsByUser(String username) {
        return auditLogRepository.findByPerformedByOrderByTimestampDesc(username);
    }

    public Page<AuditLog> getAllAuditLogs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return auditLogRepository.findAllByOrderByTimestampDesc(pageable);
    }

    public List<AuditLog> getAuditLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return auditLogRepository.findByTimestampBetweenOrderByTimestampDesc(startDate, endDate);
    }

    public List<AuditLog> getAuditLogsByEntityType(String entityType) {
        return auditLogRepository.findByEntityTypeOrderByTimestampDesc(entityType);
    }
}

package com.banking.bankingProject.controllers;

import com.banking.bankingProject.services.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    /**
     * Get dashboard statistics based on user role
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats(Authentication authentication) {
        Map<String, Object> stats = dashboardService.getDashboardStatsByRole(authentication);
        return ResponseEntity.ok(stats);
    }

    /**
     * Get recent activities for the user
     */
    @GetMapping("/activities")
    public ResponseEntity<List<Map<String, Object>>> getRecentActivities(
            @RequestParam(defaultValue = "5") int limit,
            Authentication authentication) {
        List<Map<String, Object>> activities = dashboardService.getRecentActivities(authentication, limit);
        return ResponseEntity.ok(activities);
    }

    /**
     * Get quick actions based on user permissions
     */
    @GetMapping("/quick-actions")
    public ResponseEntity<List<Map<String, String>>> getQuickActions(Authentication authentication) {
        List<Map<String, String>> actions = dashboardService.getQuickActionsByRole(authentication);
        return ResponseEntity.ok(actions);
    }

    /**
     * Get system health status (Admin only)
     */
    @GetMapping("/system-health")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getSystemHealth() {
        Map<String, Object> health = dashboardService.getSystemHealth();
        return ResponseEntity.ok(health);
    }
}

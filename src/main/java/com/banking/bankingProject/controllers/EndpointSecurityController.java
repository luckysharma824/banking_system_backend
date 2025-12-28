package com.banking.bankingProject.controllers;

import com.banking.bankingProject.entities.EndpointSecurity;
import com.banking.bankingProject.services.EndpointSecurityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/endpoint-security")
public class EndpointSecurityController {

    private final EndpointSecurityService endpointSecurityService;

    public EndpointSecurityController(EndpointSecurityService endpointSecurityService) {
        this.endpointSecurityService = endpointSecurityService;
    }

    @GetMapping
    public ResponseEntity<List<EndpointSecurity>> getAllEndpointSecurityRules() {
        return ResponseEntity.ok(endpointSecurityService.getAllEndpointSecurityRules());
    }

    @GetMapping("/enabled")
    public ResponseEntity<List<EndpointSecurity>> getEnabledEndpointSecurityRules() {
        return ResponseEntity.ok(endpointSecurityService.getEnabledEndpointSecurityRules());
    }

    @PostMapping
    public ResponseEntity<EndpointSecurity> createEndpointSecurity(@RequestBody EndpointSecurity endpointSecurity) {
        return ResponseEntity.ok(endpointSecurityService.createEndpointSecurity(endpointSecurity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EndpointSecurity> updateEndpointSecurity(
            @PathVariable Long id,
            @RequestBody EndpointSecurity endpointSecurity) {
        return ResponseEntity.ok(endpointSecurityService.updateEndpointSecurity(id, endpointSecurity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEndpointSecurity(@PathVariable Long id) {
        endpointSecurityService.deleteEndpointSecurity(id);
        return ResponseEntity.noContent().build();
    }
}

package com.banking.bankingProject.controllers;

import com.banking.bankingProject.dto.IdentityDto;
import com.banking.bankingProject.services.IdentityService;
import com.banking.bankingProject.util.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/identities")
public class IdentityController {

    private final IdentityService identityService;

    public IdentityController(IdentityService identityService) {
        this.identityService = identityService;
    }

    @PostMapping("/{customerId}")
    public ResponseEntity<Object> addIdentity(@PathVariable String customerId, @RequestBody IdentityDto identityDto) {
        return ResponseHandler.handle("success", "created", true, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Object> updateIdentity(@RequestBody IdentityDto identityDto) {
        return ResponseHandler.handle("success", "updated", true, HttpStatus.OK);
    }

}

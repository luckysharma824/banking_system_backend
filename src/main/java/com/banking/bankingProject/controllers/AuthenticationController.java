package com.banking.bankingProject.controllers;

import com.banking.bankingProject.dto.request.LoginRequestDto;
import com.banking.bankingProject.dto.response.AuthenticationResponse;
import com.banking.bankingProject.services.AuthenticationService;
import com.banking.bankingProject.util.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequestDto loginRequestDto) {
        AuthenticationResponse response = authenticationService.login(loginRequestDto);
        return ResponseHandler.handle(response, "Logged In Successfully", true, HttpStatus.OK);
    }
}

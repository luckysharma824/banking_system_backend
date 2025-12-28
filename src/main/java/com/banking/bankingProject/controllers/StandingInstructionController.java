package com.banking.bankingProject.controllers;

import com.banking.bankingProject.dto.StandingInstructionDto;
import com.banking.bankingProject.entities.StandingInstruction;
import com.banking.bankingProject.exception.BankServiceException;
import com.banking.bankingProject.services.StandingInstructionService;
import com.banking.bankingProject.util.ResponseHandler;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/standing-instructions")
public class StandingInstructionController {

    @Autowired
    private StandingInstructionService standingInstructionService;

    @PostMapping("/create")
    public ResponseEntity<Object> createStandingInstruction(@Valid @RequestBody StandingInstructionDto dto) {
        try {
            StandingInstruction instruction = standingInstructionService.createStandingInstruction(dto);
            return ResponseHandler.handle(instruction, "Standing instruction created successfully", true,
                    HttpStatus.OK);
        } catch (BankServiceException ex) {
            return ResponseHandler.handle(ex.getErrorMessage(), false, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<Object> getStandingInstructions(@PathVariable String accountNumber) {
        try {
            List<StandingInstruction> instructions = standingInstructionService.getStandingInstructions(accountNumber);
            return ResponseHandler.handle(instructions, "Standing instructions retrieved successfully", true,
                    HttpStatus.OK);
        } catch (BankServiceException ex) {
            return ResponseHandler.handle(ex.getErrorMessage(), false, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{accountNumber}/active")
    public ResponseEntity<Object> getActiveStandingInstructions(@PathVariable String accountNumber) {
        try {
            List<StandingInstruction> instructions = standingInstructionService
                    .getActiveStandingInstructions(accountNumber);
            return ResponseHandler.handle(instructions, "Active standing instructions retrieved successfully", true,
                    HttpStatus.OK);
        } catch (BankServiceException ex) {
            return ResponseHandler.handle(ex.getErrorMessage(), false, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/pause/{instructionNumber}")
    public ResponseEntity<Object> pauseStandingInstruction(@PathVariable String instructionNumber) {
        try {
            StandingInstruction instruction = standingInstructionService.pauseStandingInstruction(instructionNumber);
            return ResponseHandler.handle(instruction, "Standing instruction paused successfully", true, HttpStatus.OK);
        } catch (BankServiceException ex) {
            return ResponseHandler.handle(ex.getErrorMessage(), false, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/resume/{instructionNumber}")
    public ResponseEntity<Object> resumeStandingInstruction(@PathVariable String instructionNumber) {
        try {
            StandingInstruction instruction = standingInstructionService.resumeStandingInstruction(instructionNumber);
            return ResponseHandler.handle(instruction, "Standing instruction resumed successfully", true,
                    HttpStatus.OK);
        } catch (BankServiceException ex) {
            return ResponseHandler.handle(ex.getErrorMessage(), false, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/cancel/{instructionNumber}")
    public ResponseEntity<Object> cancelStandingInstruction(@PathVariable String instructionNumber) {
        try {
            StandingInstruction instruction = standingInstructionService.cancelStandingInstruction(instructionNumber);
            return ResponseHandler.handle(instruction, "Standing instruction cancelled successfully", true,
                    HttpStatus.OK);
        } catch (BankServiceException ex) {
            return ResponseHandler.handle(ex.getErrorMessage(), false, HttpStatus.BAD_REQUEST);
        }
    }
}

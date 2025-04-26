package com.banking.bankingProject.controllers;

import com.banking.bankingProject.dto.AddressDto;
import com.banking.bankingProject.entities.Address;
import com.banking.bankingProject.services.AddressService;
import com.banking.bankingProject.util.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PutMapping
    public ResponseEntity<Object> address(@RequestBody AddressDto addressDto) {
        Address updatedAddress = addressService.updateAddress(addressDto);
        return ResponseHandler.handle(updatedAddress, "Address has been updated now", true, HttpStatus.OK);
    }

    @GetMapping("/states")
    public ResponseEntity<Object> stateList() {
        return ResponseHandler.handle(addressService.stateList(), "State list", true, HttpStatus.OK);
    }
}

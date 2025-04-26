package com.banking.bankingProject.controllers;

import com.banking.bankingProject.dto.CustomerDto;
import com.banking.bankingProject.entities.Customer;
import com.banking.bankingProject.services.CustomerService;
import com.banking.bankingProject.util.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/create")
    public ResponseEntity<Object> createCustomer(@RequestBody CustomerDto customerDto) {
        Customer customer = customerService.createCustomer(customerDto);
        return ResponseHandler.handle(customer, "Customer Created Successfully", true, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> createCustomer(@RequestParam(required = false) String query, @RequestParam(required = false) String value) {
        List<Customer> customer = customerService.customerSearch(query, value);
        return ResponseHandler.handle(customer, "Fetched Customer successfully", true, HttpStatus.OK);
    }

}

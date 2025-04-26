package com.banking.bankingProject.controllers;

import com.banking.bankingProject.dto.RoleDto;
import com.banking.bankingProject.dto.UserDto;
import com.banking.bankingProject.entities.Role;
import com.banking.bankingProject.services.UserService;
import com.banking.bankingProject.util.ResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createUser(@RequestBody UserDto userDto) {
        LOGGER.info("User Create Request Received: {}", userDto);
        return ResponseHandler.handle(userService.createUser(userDto), "User created successfully", true, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        LOGGER.info("Get All User Request Received");
        return ResponseHandler.handle(userService.getAllUsers(), "User fetched successfully", true, HttpStatus.OK);
    }

    @GetMapping("/roles")
    public ResponseEntity<Object> roles() {
        return ResponseHandler.handle(userService.roleEnums(), "User roles successfully fetched", true, HttpStatus.OK);
    }

    @PostMapping("/roles")
    public ResponseEntity<Object> addRoles(@RequestBody RoleDto roleDto) {
        Role role = userService.addRoles(roleDto);
        return ResponseHandler.handle(role,"Roles successfully successfully", true, HttpStatus.OK);
    }
}

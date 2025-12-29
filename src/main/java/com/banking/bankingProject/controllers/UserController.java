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

import java.util.List;
import java.util.Map;

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
        return ResponseHandler.handle(userService.createUser(userDto), "User created successfully", true,
                HttpStatus.OK);
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
        return ResponseHandler.handle(role, "Roles successfully successfully", true, HttpStatus.OK);
    }

    @GetMapping("/permissions")
    public ResponseEntity<Object> permissions() {
        return ResponseHandler.handle(userService.permissions(), "Role permissions successfully fetched", true,
                HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable Long userId) {
        LOGGER.info("Get User By ID Request Received: {}", userId);
        return ResponseHandler.handle(userService.getUserById(userId), "User fetched successfully", true,
                HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable Long userId, @RequestBody UserDto userDto) {
        LOGGER.info("Update User Request Received: {} for userId: {}", userDto, userId);
        return ResponseHandler.handle(userService.updateUser(userId, userDto), "User updated successfully", true,
                HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long userId) {
        LOGGER.info("Delete User Request Received: {}", userId);
        return ResponseHandler.handle(userService.deleteUser(userId), "User deleted successfully", true, HttpStatus.OK);
    }

    @PutMapping("/password/{userId}")
    public ResponseEntity<Object> changePassword(@PathVariable Long userId,
            @RequestBody Map<String, String> passwordData) {
        LOGGER.info("Change Password Request Received for userId: {}", userId);
        return ResponseHandler.handle(
                userService.changePassword(userId, passwordData.get("oldPassword"), passwordData.get("newPassword")),
                "Password changed successfully",
                true,
                HttpStatus.OK);
    }

    @PutMapping("/roles/{userId}")
    public ResponseEntity<Object> updateUserRoles(@PathVariable Long userId,
            @RequestBody Map<String, List<RoleDto>> rolesData) {
        LOGGER.info("Update User Roles Request Received for userId: {}", userId);
        return ResponseHandler.handle(
                userService.updateUserRoles(userId, rolesData.get("roles")),
                "User roles updated successfully",
                true,
                HttpStatus.OK);
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getUserStats() {
        LOGGER.info("Get User Stats Request Received");
        return ResponseHandler.handle(userService.getUserStats(), "User statistics fetched successfully", true,
                HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchUsers(@RequestParam String search) {
        LOGGER.info("Search Users Request Received: {}", search);
        return ResponseHandler.handle(userService.searchUsers(search), "Users searched successfully", true,
                HttpStatus.OK);
    }
}

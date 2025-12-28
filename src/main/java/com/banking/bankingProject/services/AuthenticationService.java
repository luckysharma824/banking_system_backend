package com.banking.bankingProject.services;

import com.banking.bankingProject.dto.UserInfo;
import com.banking.bankingProject.dto.request.LoginRequestDto;
import com.banking.bankingProject.dto.response.AuthenticationResponse;
import com.banking.bankingProject.entities.*;
import com.banking.bankingProject.enums.ModuleEnum;
import com.banking.bankingProject.enums.RoleEnum;
import com.banking.bankingProject.exception.BankServiceException;
import com.banking.bankingProject.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtServiceImpl jwtService;
    private final UserRepository userRepository;

    public AuthenticationService(AuthenticationManager authenticationManager, JwtServiceImpl jwtService,
                                 UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    public AuthenticationResponse login(LoginRequestDto loginRequestDto) {
        try {
            log.info("Login attempt for user: {}", loginRequestDto.getUsername());

            // Validate input
            if (loginRequestDto.getUsername() == null || loginRequestDto.getUsername().trim().isEmpty()) {
                throw new BankServiceException("EC-001", "Username cannot be empty", null);
            }
            if (loginRequestDto.getPassword() == null || loginRequestDto.getPassword().trim().isEmpty()) {
                throw new BankServiceException("EC-002", "Password cannot be empty", null);
            }

            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDto.getUsername(),
                            loginRequestDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Fetch user details
            User user = userRepository.findByUsername(loginRequestDto.getUsername());
            if (user == null) {
                throw new BankServiceException("EC-003", "User not found", null);
            }

            // Build permissions map
            Map<ModuleEnum, List<String>> permissionMap = new HashMap<>();
            for (Role role : user.getRoles()) {
                if (role.getModulePermissions() != null) {
                    for (ModulePermission modulePermission : role.getModulePermissions()) {
                        ModuleEnum moduleEnum = modulePermission.getModule().getName();

                        List<String> permissions = modulePermission
                                .getPermissions()
                                .stream()
                                .map(permission -> permission.getName() + "_" + moduleEnum)
                                .toList();

                        // Merge permissions if module already exists
                        permissionMap.merge(moduleEnum, permissions, (existing, newPerms) -> {
                            Set<String> merged = new HashSet<>(existing);
                            merged.addAll(newPerms);
                            return merged.stream().toList();
                        });
                    }
                }
            }

            // Extract role names
            Set<RoleEnum> roleEnums = user.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet());

            // Build user info
            UserInfo userInfo = new UserInfo(
                    user.getId(),
                    user.getUsername(),
                    roleEnums,
                    null, // Email can be added if available in User entity
                    user.getUsername() // Can be replaced with actual full name if available
            );

            // Generate JWT token
            String token = jwtService.generateToken(user);
            Long expiresIn = 86400000L; // 24 hours in milliseconds
            LocalDateTime loginTime = LocalDateTime.now();

            log.info("Login successful for user: {} with roles: {}", user.getUsername(), roleEnums);

            return AuthenticationResponse.builder()
                    .token(token)
                    .expiresIn(expiresIn)
                    .permissions(permissionMap)
                    .userInfo(userInfo)
                    .loginTime(loginTime)
                    .build();

        } catch (BadCredentialsException e) {
            log.error("Invalid credentials for user: {}", loginRequestDto.getUsername());
            throw new BankServiceException("EC-004", "Invalid username or password", null);
        } catch (BankServiceException e) {
            log.error("Login error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during login: {}", e.getMessage(), e);
            throw new BankServiceException("EC-005", "Login failed. Please try again later.", null);
        }
    }
}

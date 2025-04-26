package com.banking.bankingProject.services;

import com.banking.bankingProject.dto.request.LoginRequestDto;
import com.banking.bankingProject.dto.response.AuthenticationResponse;
import com.banking.bankingProject.entities.*;
import com.banking.bankingProject.enums.ModuleEnum;
import com.banking.bankingProject.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtServiceImpl jwtService;
    private final UserRepository userRepository;

    public AuthenticationService(AuthenticationManager authenticationManager, JwtServiceImpl jwtService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    public AuthenticationResponse login(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userRepository.findByUsername(loginRequestDto.getUsername());

        Map<ModuleEnum, List<String>> permissionMap = new HashMap<>();

        for (Role role : user.getRoles()) {
            for (ModulePermission modulePermission: role.getModulePermissions()) {
                ModuleEnum moduleEnum = modulePermission.getModule().getName();

                List<String> permissions = modulePermission
                        .getPermissions()
                        .stream()
                        .map(permission -> permission.getName() + "_" + moduleEnum)
                        .toList();

                permissionMap.put(moduleEnum, permissions);
            }
        }

        String token = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(token).permissions(permissionMap).build();
    }
}

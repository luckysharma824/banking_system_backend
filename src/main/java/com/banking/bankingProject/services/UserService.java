package com.banking.bankingProject.services;

import com.banking.bankingProject.dto.ModulePermissionDto;
import com.banking.bankingProject.dto.RoleDto;
import com.banking.bankingProject.dto.UserDto;
import com.banking.bankingProject.entities.*;
import com.banking.bankingProject.entities.Module;
import com.banking.bankingProject.enums.ModuleEnum;
import com.banking.bankingProject.enums.PermissionEnum;
import com.banking.bankingProject.enums.RoleEnum;
import com.banking.bankingProject.exception.BankServiceException;
import com.banking.bankingProject.repositories.*;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final ModuleRepository moduleRepository;
    private final ModulePermissionRepository modulePermissionRepository;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, PermissionRepository permissionRepository, ModuleRepository moduleRepository, ModulePermissionRepository modulePermissionRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.moduleRepository = moduleRepository;
        this.modulePermissionRepository = modulePermissionRepository;
    }

    public User createUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        List<RoleEnum> roleEnums = userDto.getRoles().stream().map(RoleDto::getName).toList();
        List<Role> roles = roleRepository.findByNameIn(roleEnums);

        LOGGER.info("Data fetch for roles are: {}", roles);

        user.setRoles(new HashSet<>(roles));
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public RoleEnum[] roleEnums() {
        return RoleEnum.values();
    }

    public Role addRoles(RoleDto roleDto) {

        Set<ModulePermission> modulePermissions = new HashSet<>();
        for (ModulePermissionDto modulePermissionDto: roleDto.getModulePermissions()) {

            Module module = moduleRepository.findByName(modulePermissionDto.getModuleName());
            if (module == null) {
                throw new BankServiceException("EC-101", "Module not present", null);
            }

            List<Permission> permissions = permissionRepository.findByNameIn(modulePermissionDto.getPermissions());
            if (permissions.size() != modulePermissionDto.getPermissions().size()) {
                throw new BankServiceException("EC-102", "Some permissions not present", null);
            }

            ModulePermission modulePermission = new ModulePermission();
            modulePermission.setModule(module);
            modulePermission.setPermissions(new HashSet<>(permissions));
            modulePermissionRepository.save(modulePermission);

            modulePermissions.add(modulePermission);

        }
        Role role = roleRepository.findByName(roleDto.getName());
        if (role == null) {
            role = new Role();
            role.setName(roleDto.getName());
        }
        role.setModulePermissions(modulePermissions);
        return roleRepository.save(role);
    }

    /*@PostConstruct
    public void init() {
        List<Module> modules = Arrays.stream(ModuleEnum.values()).map(moduleEnum -> {
            Module module = new Module();
            module.setName(moduleEnum);
            return module;
        }).toList();

        List<Permission> permissions = Arrays.stream(PermissionEnum.values()).map(permissionEnum -> {
            Permission permission = new Permission();
            permission.setName(permissionEnum);
            return permission;
        }).toList();

        moduleRepository.saveAll(modules);
        permissionRepository.saveAll(permissions);

    }*/

}

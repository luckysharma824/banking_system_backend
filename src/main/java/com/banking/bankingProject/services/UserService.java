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
    private final EndpointSecurityRepository endpointSecurityRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository,
            PermissionRepository permissionRepository, ModuleRepository moduleRepository,
            ModulePermissionRepository modulePermissionRepository,
            EndpointSecurityRepository endpointSecurityRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.moduleRepository = moduleRepository;
        this.modulePermissionRepository = modulePermissionRepository;
        this.endpointSecurityRepository = endpointSecurityRepository;
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
        for (ModulePermissionDto modulePermissionDto : roleDto.getModulePermissions()) {

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

    public List<RoleDto> permissions() {
        List<Role> roles = roleRepository.findAll();
        // Build permissions map
        List<RoleDto> roleDtos = new ArrayList<>();
        for (Role role : roles) {

            RoleDto roleDto = new RoleDto();
            roleDto.setName(role.getName());

            if (role.getModulePermissions() != null) {
                List<ModulePermissionDto> modulePermissionDtos = new ArrayList<>();

                for (ModulePermission modulePermission : role.getModulePermissions()) {
                    ModulePermissionDto modulePermissionDto = new ModulePermissionDto();
                    modulePermissionDto.setModuleName(modulePermission.getModule().getName());
                    List<PermissionEnum> perms = modulePermission.getPermissions()
                            .stream()
                            .map(Permission::getName)
                            .toList();
                    modulePermissionDto.setPermissions(perms);
                    modulePermissionDtos.add(modulePermissionDto);
                }

                roleDto.setModulePermissions(modulePermissionDtos);
            }
            roleDtos.add(roleDto);
        }
        return roleDtos;
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BankServiceException("EC-103", "User not found", null));
    }

    public User updateUser(Long userId, UserDto userDto) {
        User user = getUserById(userId);

        if (userDto.getUsername() != null && !userDto.getUsername().isEmpty()) {
            user.setUsername(userDto.getUsername());
        }

        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        if (userDto.getRoles() != null && !userDto.getRoles().isEmpty()) {
            List<RoleEnum> roleEnums = userDto.getRoles().stream().map(RoleDto::getName).toList();
            List<Role> roles = roleRepository.findByNameIn(roleEnums);
            user.setRoles(new HashSet<>(roles));
        }

        return userRepository.save(user);
    }

    public Map<String, String> deleteUser(Long userId) {
        User user = getUserById(userId);
        userRepository.delete(user);
        return Map.of("message", "User deleted successfully", "username", user.getUsername());
    }

    public Map<String, String> changePassword(Long userId, String oldPassword, String newPassword) {
        User user = getUserById(userId);

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BankServiceException("EC-104", "Old password is incorrect", null);
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return Map.of("message", "Password changed successfully");
    }

    public User updateUserRoles(Long userId, List<RoleDto> roleDtos) {
        User user = getUserById(userId);

        List<RoleEnum> roleEnums = roleDtos.stream().map(RoleDto::getName).toList();
        List<Role> roles = roleRepository.findByNameIn(roleEnums);

        user.setRoles(new HashSet<>(roles));
        return userRepository.save(user);
    }

    public Map<String, Object> getUserStats() {
        List<User> allUsers = userRepository.findAll();
        Map<RoleEnum, Long> roleDistribution = new HashMap<>();

        for (RoleEnum roleEnum : RoleEnum.values()) {
            roleDistribution.put(roleEnum, 0L);
        }

        for (User user : allUsers) {
            for (Role role : user.getRoles()) {
                roleDistribution.merge(role.getName(), 1L, Long::sum);
            }
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", allUsers.size());
        stats.put("roleDistribution", roleDistribution);
        stats.put("activeUsers", allUsers.size()); // Can be enhanced with actual active status

        return stats;
    }

    public List<User> searchUsers(String search) {
        // Search by username containing the search term (case-insensitive)
        // Uses database-level filtering for better performance
        if (search == null || search.trim().isEmpty()) {
            return userRepository.findAll();
        }
        return userRepository.searchByUsername(search.trim());
    }

    @PostConstruct
    public void init() {
        // Get existing modules from database
        List<Module> existingModules = moduleRepository.findAll();
        Set<ModuleEnum> existingModuleNames = existingModules.stream()
                .map(Module::getName)
                .collect(java.util.stream.Collectors.toSet());

        // Only create modules that don't exist
        List<Module> modulesToSave = Arrays.stream(ModuleEnum.values())
                .filter(moduleEnum -> !existingModuleNames.contains(moduleEnum))
                .map(moduleEnum -> {
                    Module module = new Module();
                    module.setName(moduleEnum);
                    return module;
                }).toList();

        if (!modulesToSave.isEmpty()) {
            moduleRepository.saveAll(modulesToSave);
            LOGGER.info("Added {} new modules to database", modulesToSave.size());
        }

        // Get existing permissions from database
        List<Permission> existingPermissions = permissionRepository.findAll();
        Set<PermissionEnum> existingPermissionNames = existingPermissions.stream()
                .map(Permission::getName)
                .collect(java.util.stream.Collectors.toSet());

        // Only create permissions that don't exist
        List<Permission> permissionsToSave = Arrays.stream(PermissionEnum.values())
                .filter(permissionEnum -> !existingPermissionNames.contains(permissionEnum))
                .map(permissionEnum -> {
                    Permission permission = new Permission();
                    permission.setName(permissionEnum);
                    return permission;
                }).toList();

        if (!permissionsToSave.isEmpty()) {
            permissionRepository.saveAll(permissionsToSave);
            LOGGER.info("Added {} new permissions to database", permissionsToSave.size());
        }

        // Get existing roles from database
        List<Role> existingRoles = roleRepository.findAll();
        Set<RoleEnum> existingRoleNames = existingRoles.stream()
                .map(Role::getName)
                .collect(java.util.stream.Collectors.toSet());

        // Only create roles that don't exist
        List<RoleEnum> rolesToCreate = Arrays.stream(RoleEnum.values())
                .filter(roleEnum -> !existingRoleNames.contains(roleEnum))
                .toList();

        if (!rolesToCreate.isEmpty()) {
            for (RoleEnum roleEnum : rolesToCreate) {
                Role role = new Role();
                role.setName(roleEnum);

                // Add default module permissions for specific roles
                Set<ModulePermission> modulePermissions = new HashSet<>();

                if (roleEnum == RoleEnum.ROLE_ADMIN) {
                    // Admin gets all permissions for all modules
                    ModuleEnum[] adminModules = { ModuleEnum.USER, ModuleEnum.TRANSACTION, ModuleEnum.CUSTOMER,
                            ModuleEnum.ACCOUNT };
                    PermissionEnum[] allPermissions = { PermissionEnum.DELETE, PermissionEnum.VIEW,
                            PermissionEnum.CREATE, PermissionEnum.EDIT };

                    for (ModuleEnum moduleEnum : adminModules) {
                        Module module = moduleRepository.findByName(moduleEnum);
                        List<Permission> permissions = permissionRepository.findByNameIn(Arrays.asList(allPermissions));

                        ModulePermission modulePermission = new ModulePermission();
                        modulePermission.setModule(module);
                        modulePermission.setPermissions(new HashSet<>(permissions));
                        modulePermissionRepository.save(modulePermission);
                        modulePermissions.add(modulePermission);
                    }
                } else if (roleEnum == RoleEnum.ROLE_CASHIER) {
                    // Cashier permissions: VIEW for CUSTOMER and ACCOUNT, VIEW+CREATE for
                    // TRANSACTION
                    Module customerModule = moduleRepository.findByName(ModuleEnum.CUSTOMER);
                    List<Permission> viewPermission = permissionRepository
                            .findByNameIn(List.of(PermissionEnum.VIEW));

                    ModulePermission customerModulePermission = new ModulePermission();
                    customerModulePermission.setModule(customerModule);
                    customerModulePermission.setPermissions(new HashSet<>(viewPermission));
                    modulePermissionRepository.save(customerModulePermission);
                    modulePermissions.add(customerModulePermission);

                    Module accountModule = moduleRepository.findByName(ModuleEnum.ACCOUNT);
                    ModulePermission accountModulePermission = new ModulePermission();
                    accountModulePermission.setModule(accountModule);
                    accountModulePermission.setPermissions(new HashSet<>(viewPermission));
                    modulePermissionRepository.save(accountModulePermission);
                    modulePermissions.add(accountModulePermission);

                    Module transactionModule = moduleRepository.findByName(ModuleEnum.TRANSACTION);
                    List<Permission> transactionPermissions = permissionRepository.findByNameIn(
                            Arrays.asList(PermissionEnum.VIEW, PermissionEnum.CREATE));
                    ModulePermission transactionModulePermission = new ModulePermission();
                    transactionModulePermission.setModule(transactionModule);
                    transactionModulePermission.setPermissions(new HashSet<>(transactionPermissions));
                    modulePermissionRepository.save(transactionModulePermission);
                    modulePermissions.add(transactionModulePermission);
                }

                role.setModulePermissions(modulePermissions);
                roleRepository.save(role);
            }
            LOGGER.info("Added {} new roles to database with module permissions", rolesToCreate.size());
        }

        // Create default admin user if not exists
        String defaultAdminUsername = "admin";
        if (userRepository.findByUsername(defaultAdminUsername) == null) {
            User adminUser = new User();
            adminUser.setUsername(defaultAdminUsername);
            adminUser.setPassword(passwordEncoder.encode("admin123")); // Default password

            Role adminRole = roleRepository.findByName(RoleEnum.ROLE_ADMIN);
            if (adminRole != null) {
                adminUser.setRoles(Set.of(adminRole));
                userRepository.save(adminUser);
                LOGGER.info("Created default admin user with username: {}", defaultAdminUsername);
            } else {
                LOGGER.warn("ROLE_ADMIN not found, skipping admin user creation");
            }
        }

        // Initialize endpoint security rules if not present
        if (endpointSecurityRepository.count() == 0) {
            LOGGER.info("Initializing endpoint security rules...");
            initializeEndpointSecurityRules();
            LOGGER.info("Endpoint security rules initialized successfully");
        } else {
            LOGGER.info("Endpoint security rules already exist, skipping initialization");
        }
    }

    private void initializeEndpointSecurityRules() {
        // Public endpoints
        createEndpointRule("/auth/login", null, Set.of(), 1, true);
        createEndpointRule("/users/roles", null, Set.of(), 2, true);
        createEndpointRule("/**", "OPTIONS", Set.of(), 3, true);
        createEndpointRule("/users/permissions", null, Set.of(), 4, true);

        // Endpoint Security management (ADMIN only)
        createEndpointRule("/endpoint-security/**", null, Set.of(RoleEnum.ROLE_ADMIN), 5, false);

        // User management endpoints (ADMIN only)
        createEndpointRule("/users", null, Set.of(RoleEnum.ROLE_ADMIN), 10, false);
        createEndpointRule("/users/create", null, Set.of(RoleEnum.ROLE_ADMIN), 11, false);

        // Customer create (ADMIN, CLERK, MANAGER)
        createEndpointRule("/customers/create", null,
                Set.of(RoleEnum.ROLE_ADMIN, RoleEnum.ROLE_CLERK, RoleEnum.ROLE_MANAGER), 20, false);

        // Customer search (ADMIN, CLERK, MANAGER, CASHIER)
        createEndpointRule("/customers/search", null,
                Set.of(RoleEnum.ROLE_ADMIN, RoleEnum.ROLE_CLERK, RoleEnum.ROLE_MANAGER, RoleEnum.ROLE_CASHIER),
                21, false);

        // Account balance (All roles)
        createEndpointRule("/accounts/balance/**", null,
                Set.of(RoleEnum.ROLE_ADMIN, RoleEnum.ROLE_CLERK, RoleEnum.ROLE_MANAGER, RoleEnum.ROLE_CASHIER),
                30, false);

        // Accounts GET (CASHIER can view)
        createEndpointRule("/accounts/**", "GET",
                Set.of(RoleEnum.ROLE_ADMIN, RoleEnum.ROLE_CLERK, RoleEnum.ROLE_MANAGER, RoleEnum.ROLE_CASHIER),
                31, false);

        // Accounts all methods (ADMIN, CLERK, MANAGER)
        createEndpointRule("/accounts/**", null,
                Set.of(RoleEnum.ROLE_ADMIN, RoleEnum.ROLE_CLERK, RoleEnum.ROLE_MANAGER),
                32, false);

        // Transactions (ADMIN, CASHIER)
        createEndpointRule("/transactions/**", null,
                Set.of(RoleEnum.ROLE_ADMIN, RoleEnum.ROLE_CASHIER),
                40, false);
    }

    private void createEndpointRule(String urlPattern, String httpMethod, Set<RoleEnum> roles, int priority,
            boolean permitAll) {
        EndpointSecurity rule = new EndpointSecurity();
        rule.setUrlPattern(urlPattern);
        rule.setHttpMethod(httpMethod);
        rule.setAllowedRoles(roles);
        rule.setPriority(priority);
        rule.setPermitAll(permitAll);
        rule.setEnabled(true);
        endpointSecurityRepository.save(rule);
        LOGGER.debug("Created endpoint rule: {} {} - Priority: {}, PermitAll: {}, Roles: {}",
                httpMethod != null ? httpMethod : "ALL", urlPattern, priority, permitAll, roles);
    }

}

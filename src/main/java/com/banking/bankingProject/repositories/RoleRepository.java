package com.banking.bankingProject.repositories;

import com.banking.bankingProject.entities.Role;
import com.banking.bankingProject.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findByNameIn(List<RoleEnum> names);
    Role findByName(RoleEnum name);
}

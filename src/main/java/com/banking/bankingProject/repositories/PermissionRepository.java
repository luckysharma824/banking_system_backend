package com.banking.bankingProject.repositories;

import com.banking.bankingProject.entities.Module;
import com.banking.bankingProject.entities.Permission;
import com.banking.bankingProject.enums.PermissionEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    List<Permission> findByNameIn(List<PermissionEnum> names);
}

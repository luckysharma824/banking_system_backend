package com.banking.bankingProject.repositories;

import com.banking.bankingProject.entities.ModulePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModulePermissionRepository extends JpaRepository<ModulePermission, Long> {
}

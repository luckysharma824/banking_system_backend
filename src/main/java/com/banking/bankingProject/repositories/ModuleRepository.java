package com.banking.bankingProject.repositories;

import com.banking.bankingProject.entities.Module;
import com.banking.bankingProject.enums.ModuleEnum;
import com.banking.bankingProject.enums.PermissionEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
    List<Module> findByNameIn(List<ModuleEnum> names);
    Module findByName(ModuleEnum name);
}

package com.banking.bankingProject.repositories;

import com.banking.bankingProject.entities.EndpointSecurity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EndpointSecurityRepository extends JpaRepository<EndpointSecurity, Long> {

    List<EndpointSecurity> findByEnabledTrueOrderByPriorityAsc();
}

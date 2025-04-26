package com.banking.bankingProject.repositories;

import com.banking.bankingProject.entities.Identity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdentityRepository extends JpaRepository<Identity,Long> {
}

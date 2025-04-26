package com.banking.bankingProject.services;

import com.banking.bankingProject.repositories.IdentityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IdentityService {
    @Autowired
    private IdentityRepository identityRepository;

}

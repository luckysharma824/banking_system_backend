package com.banking.bankingProject.services;

import com.banking.bankingProject.entities.EndpointSecurity;
import com.banking.bankingProject.repositories.EndpointSecurityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EndpointSecurityService {

    private final EndpointSecurityRepository endpointSecurityRepository;

    public EndpointSecurityService(EndpointSecurityRepository endpointSecurityRepository) {
        this.endpointSecurityRepository = endpointSecurityRepository;
    }

    public List<EndpointSecurity> getEnabledEndpointSecurityRules() {
        return endpointSecurityRepository.findByEnabledTrueOrderByPriorityAsc();
    }

    public EndpointSecurity createEndpointSecurity(EndpointSecurity endpointSecurity) {
        return endpointSecurityRepository.save(endpointSecurity);
    }

    public EndpointSecurity updateEndpointSecurity(Long id, EndpointSecurity endpointSecurity) {
        endpointSecurity.setId(id);
        return endpointSecurityRepository.save(endpointSecurity);
    }

    public void deleteEndpointSecurity(Long id) {
        endpointSecurityRepository.deleteById(id);
    }

    public List<EndpointSecurity> getAllEndpointSecurityRules() {
        return endpointSecurityRepository.findAll();
    }
}

package com.banking.bankingProject.services;

import com.banking.bankingProject.entities.EndpointSecurity;
import com.banking.bankingProject.enums.RoleEnum;
import com.banking.bankingProject.repositories.EndpointSecurityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EndpointSecurityServiceTest {

    @Mock
    private EndpointSecurityRepository endpointSecurityRepository;

    @InjectMocks
    private EndpointSecurityService endpointSecurityService;

    private EndpointSecurity testRule;

    @BeforeEach
    void setUp() {
        testRule = new EndpointSecurity();
        testRule.setId(1L);
        testRule.setUrlPattern("/test/**");
        testRule.setHttpMethod("GET");
        testRule.setAllowedRoles(Set.of(RoleEnum.ROLE_ADMIN));
        testRule.setPriority(10);
        testRule.setPermitAll(false);
        testRule.setEnabled(true);
    }

    @Test
    void testGetEnabledEndpointSecurityRules() {
        // Arrange
        List<EndpointSecurity> expectedRules = Arrays.asList(testRule);
        when(endpointSecurityRepository.findByEnabledTrueOrderByPriorityAsc())
                .thenReturn(expectedRules);

        // Act
        List<EndpointSecurity> actualRules = endpointSecurityService.getEnabledEndpointSecurityRules();

        // Assert
        assertNotNull(actualRules);
        assertEquals(1, actualRules.size());
        assertEquals(testRule.getUrlPattern(), actualRules.get(0).getUrlPattern());
        verify(endpointSecurityRepository, times(1)).findByEnabledTrueOrderByPriorityAsc();
    }

    @Test
    void testCreateEndpointSecurity() {
        // Arrange
        when(endpointSecurityRepository.save(any(EndpointSecurity.class)))
                .thenReturn(testRule);

        // Act
        EndpointSecurity created = endpointSecurityService.createEndpointSecurity(testRule);

        // Assert
        assertNotNull(created);
        assertEquals(testRule.getUrlPattern(), created.getUrlPattern());
        verify(endpointSecurityRepository, times(1)).save(testRule);
    }

    @Test
    void testUpdateEndpointSecurity() {
        // Arrange
        Long id = 1L;
        when(endpointSecurityRepository.save(any(EndpointSecurity.class)))
                .thenReturn(testRule);

        // Act
        EndpointSecurity updated = endpointSecurityService.updateEndpointSecurity(id, testRule);

        // Assert
        assertNotNull(updated);
        assertEquals(id, updated.getId());
        verify(endpointSecurityRepository, times(1)).save(testRule);
    }

    @Test
    void testDeleteEndpointSecurity() {
        // Arrange
        Long id = 1L;
        doNothing().when(endpointSecurityRepository).deleteById(id);

        // Act
        endpointSecurityService.deleteEndpointSecurity(id);

        // Assert
        verify(endpointSecurityRepository, times(1)).deleteById(id);
    }

    @Test
    void testGetAllEndpointSecurityRules() {
        // Arrange
        List<EndpointSecurity> expectedRules = Arrays.asList(testRule);
        when(endpointSecurityRepository.findAll()).thenReturn(expectedRules);

        // Act
        List<EndpointSecurity> actualRules = endpointSecurityService.getAllEndpointSecurityRules();

        // Assert
        assertNotNull(actualRules);
        assertEquals(1, actualRules.size());
        verify(endpointSecurityRepository, times(1)).findAll();
    }
}

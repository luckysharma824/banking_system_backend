package com.banking.bankingProject.services;

import com.banking.bankingProject.dto.CustomerDto;
import com.banking.bankingProject.entities.Address;
import com.banking.bankingProject.entities.Customer;
import com.banking.bankingProject.entities.Identity;
import com.banking.bankingProject.repositories.AddressRepository;
import com.banking.bankingProject.repositories.CustomerRepository;
import com.banking.bankingProject.repositories.IdentityRepository;
import com.banking.bankingProject.util.Utility;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final IdentityRepository identityRepository;

    public CustomerService(
            CustomerRepository customerRepository,
            AddressRepository addressRepository,
            IdentityRepository identityRepository
    ) {
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
        this.identityRepository = identityRepository;
    }

    public Customer createCustomer(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setFirstName(customerDto.getFirstName());
        customer.setMiddleName(customerDto.getMiddleName());
        customer.setLastName(customerDto.getLastName());
        customer.setFather(customerDto.getFather());
        customer.setMother(customerDto.getMother());
        customer.setPhone(customerDto.getPhone());
        customer.setEmail(customerDto.getEmail());
        customer.setCustomerId("CUS_" + Utility.getUuid(""));
        customerRepository.save(customer);

        List<Address> addresses = customerDto
                .getAddresses()
                .stream()
                .map(addressDto -> {
                    Address address = new Address();
                    address.setLine1(addressDto.getLine1());
                    address.setLine2(addressDto.getLine2());
                    address.setLine3(addressDto.getLine3());
                    address.setPostalCode(addressDto.getPostalCode());
                    address.setState(addressDto.getState());
                    address.setAddressType(addressDto.getAddressType());
                    address.setCustomer(customer);
                    return address;
                })
                .toList();
        addressRepository.saveAll(addresses);

        List<Identity> identities = customerDto
                .getIdentities()
                .stream()
                .map(identityDto -> {
                    Identity identity = new Identity();
                    identity.setIdentityType(identityDto.getIdentityType());
                    identity.setIdentityNumber(identityDto.getIdentityNumber());
                    identity.setCustomer(customer);
                    return identity;
                }).toList();

        identityRepository.saveAll(identities);
        return customer;
    }

    public List<Customer> customerSearch(String query, String value) {
        if ("name".equals(query)) {
            return customerRepository.searchByNameLike(value);
        }
        if ("email".equals(query)) {
            return customerRepository.searchByEmailLike(value);
        }
        if ("phone".equals(query)) {
            return customerRepository.searchByPhoneLike(value);
        }
        return customerRepository.findAll();
    }
}

package com.banking.bankingProject.services;

import com.banking.bankingProject.dto.AddressDto;
import com.banking.bankingProject.entities.Address;
import com.banking.bankingProject.entities.Customer;
import com.banking.bankingProject.enums.StateEnum;
import com.banking.bankingProject.repositories.AddressRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Address updateAddress(AddressDto addressDto) {
        Optional<Address> optionalAddress = addressRepository.findById(addressDto.getId());
        return optionalAddress
                .map(address -> addOrUpdateAddress(address, addressDto))
                .orElse(null);
    }

    private Address addOrUpdateAddress(Address address, AddressDto addressDto) {
        address.setLine1(addressDto.getLine1());
        address.setLine2(addressDto.getLine2());
        address.setLine3(addressDto.getLine3());
        address.setPostalCode(addressDto.getPostalCode());
        address.setState(addressDto.getState());
        address.setAddressType(addressDto.getAddressType());
        return addressRepository.save(address);
    }

    public StateEnum[] stateList() {
        return StateEnum.values();
    }

}

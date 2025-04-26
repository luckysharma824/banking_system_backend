package com.banking.bankingProject.repositories;

import com.banking.bankingProject.entities.Customer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByCustomerId(String customerId);

    @Query(value = "select e from Customer e where concat(e.firstName, ' ', e.middleName, ' ', e.lastName) like :name")
    List<Customer> searchByNameLike(String name);

    @Query(value = "select e from Customer e where e.email like :email")
    List<Customer> searchByEmailLike(String email);

    @Query(value = "select e from Customer e where e.phone like :phone")
    List<Customer> searchByPhoneLike(String phone);
}

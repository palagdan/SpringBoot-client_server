package com.palagincom.server.dao;

import com.palagincom.server.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    List<Customer> findByName(String name);

    List<Customer> findBySurname(String surname);

    Optional<Customer> findByMail(String mail);
}

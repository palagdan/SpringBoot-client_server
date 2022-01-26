package com.palagincom.server.business;

import com.palagincom.server.dao.CustomerRepository;
import com.palagincom.server.domain.Customer;
import com.palagincom.server.domain.Product;
import com.palagincom.server.exceptions.CustomerNotFound;
import com.palagincom.server.exceptions.ProductDoesNotExist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Service
public class CustomerService{

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    public List<Customer> readAll(){
        return customerRepository.findAll();
    }

    public Optional<Customer> findByUsername(String id){
        return customerRepository.findById(id);
    }
    public List<Customer> findByName(String name){
        return customerRepository.findByName(name);
    }
    public List<Customer> findBySurname(String surname){
        return customerRepository.findBySurname(surname);
    }
    public Optional<Customer> findByMail(String mail){return customerRepository.findByMail(mail);}

    @Transactional
    public Customer create(Customer customer) throws Exception {
        if (customerRepository.findById(customer.getUsername()).isPresent())
            throw new InstanceAlreadyExistsException("Customer with username: " + customer.getUsername() + " already exists");
        return customerRepository.save(customer);
    }

    @Transactional
    public void  update(String id, Customer customer)throws Exception{
        Optional <Customer> find = customerRepository.findById(id);

        if(find.isEmpty()) throw new CustomerNotFound("Customer with " + customer.getUsername()  + " not found");

        customerRepository.save(customer);

    }


    @Transactional
    public void delete(String username) throws Exception{
        Optional<Customer> customer = findByUsername(username);
        if(customer.isEmpty())
            throw new CustomerNotFound("Customer with username " + username  + " not found.");
        customerRepository.delete(customer.get());
    }


}

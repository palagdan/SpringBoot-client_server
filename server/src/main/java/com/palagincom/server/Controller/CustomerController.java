package com.palagincom.server.Controller;

import com.palagincom.server.business.CustomerService;
import com.palagincom.server.domain.Customer;
import com.palagincom.server.domain.Product;

import com.palagincom.server.exceptions.CustomerNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    CustomerController(CustomerService customerService){
        this.customerService = customerService;
    }

     @PostMapping("/customer")
     @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Customer> addCustomer(@RequestBody Customer customer)throws Exception{
       try{
           Customer customerCreated = customerService.create(customer);
           return ResponseEntity.status(HttpStatus.CREATED).body(customerCreated);
       }catch(InstanceAlreadyExistsException e){
           throw new ResponseStatusException(HttpStatus.CONFLICT);
       }
    }

    @GetMapping("/customer")
    List<Customer> findAllCustomer(){
        return customerService.readAll();
    }


    @GetMapping("/customer/{id}")
    public Customer customerFindById(@PathVariable String  id){
        Optional<Customer> customer = customerService.findByUsername(id);
        return customer.orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
    }


    @GetMapping("/customer/name/{name}")
    public List<Customer> customerFindByName(@PathVariable String  name){
        List<Customer> customer = customerService.findByName(name);
        if(customer.isEmpty())throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return customer;
    }

    @GetMapping("/customer/mail/{mail}")
    public Customer customerFindByMail(@PathVariable String mail){
        Optional<Customer> customer = customerService.findByMail(mail);
        if(customer.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return customer.get();
    }
    @GetMapping("/customer/surname/{surname}")
    public List<Customer> customerFindBySurname(@PathVariable String  surname){
        List<Customer> customer = customerService.findBySurname(surname);
        if(customer.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return customer;
    }

    @DeleteMapping("/customer/{id}")
    public void deleteCustomer( @PathVariable String id)throws Exception{
      try{
          customerService.delete(id);
      }catch(CustomerNotFound e) {
          throw new ResponseStatusException(HttpStatus.NOT_FOUND);
      }
    }

    @PutMapping("/customer/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCustomer(@PathVariable String id,@RequestBody Customer customer)throws Exception{
       try{
           customerService.update(id,customer);

       }catch (CustomerNotFound e){
           throw new ResponseStatusException(HttpStatus.NOT_FOUND);
       }
    }


}

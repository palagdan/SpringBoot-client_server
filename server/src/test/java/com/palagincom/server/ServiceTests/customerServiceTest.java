package com.palagincom.server.ServiceTests;

import com.palagincom.server.business.CustomerService;
import com.palagincom.server.dao.CustomerRepository;
import com.palagincom.server.domain.Customer;
import com.palagincom.server.domain.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class customerServiceTest {

    @InjectMocks
    CustomerService customerService;

    @Mock
    CustomerRepository customerRepository;

    @Test
    public void testReadAll() {
        Customer customer = new Customer("artem13");
        Customer customer1 = new Customer("danya45");
        List<Customer> customers  = List.of(customer, customer1);

        Mockito.when(customerRepository.findAll()).thenReturn(customers);

        Collection<Customer> customerCollection = customerService.readAll();

        assertEquals(2,customerCollection.size());
        verify(customerRepository, times(1)).findAll();
    }


    @Test
    public void testCreate() throws Exception {
        Customer customer = new Customer("dan");

        customerService.create(customer);

        verify(customerRepository,times(1)).save(any());
        verify(customerRepository, times(1)).save(any(Customer.class));
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    public void findByUsername(){
        Customer customer = new Customer("dan");

        Mockito.when(customerRepository.findById("dan")).thenReturn(Optional.of(customer));

        Customer customerFound = customerService.findByUsername("dan").get();
        assertEquals(customerFound,customer);
    }
    @Test
    public void findBySurname(){
        Customer customer = new Customer("dan", "Daniil", null,"Palagin", "dan.palagin@cvut.cz");
        Customer customer1 = new Customer("artem12", "Artem", null,"Palagin", "artem.palagin@cvut.cz");

        List<Customer> customerList = List.of(customer,customer1);
        Mockito.when(customerRepository.findBySurname("Palagin")).thenReturn(customerList);

        List<Customer> customers = customerService.findBySurname("Palagin");

        assertEquals(customerList,customers);
    }

    @Test
    public void findByName(){
        Customer customer = new Customer("dan", "Daniil", null,"Palagin", "dan.palagin@cvut.cz");
        Customer customer1 = new Customer("artem12", "Daniil", null,"Surkov", "artem.palagin@cvut.cz");

        List<Customer> customerList = List.of(customer,customer1);
        Mockito.when(customerRepository.findByName("Daniil")).thenReturn(customerList);

        List<Customer> customers = customerService.findByName("Daniil");

        assertEquals(customerList,customers);
    }

    @Test
    public void findByMail(){
        Customer customer = new Customer("dan", "Daniil", null,"Palagin", "dan.palagin@cvut.cz");

        Mockito.when(customerRepository.findByMail("dan.palagin@cvut.cz")).thenReturn(Optional.of(customer));

       Customer  customerFound = customerService.findByMail("dan.palagin@cvut.cz").get();

        assertEquals(customer,customerFound);
    }

    @Test
    public void testDelete() throws Exception {
        Customer customer = new Customer("dan", "Daniil", null,"Palagin", "dan.palagin@cvut.cz");

        Mockito.when(customerRepository.findById("dan")).thenReturn(Optional.of(customer));
        Mockito.when(customerRepository.findById(not(eq("dan")))).thenReturn(Optional.empty());
        Mockito.doNothing().when(customerRepository).delete(customer);

        customerService.delete(customer.getUsername());

        verify(customerRepository,Mockito.times(1)).delete(any());
        verify(customerRepository, Mockito.times(1)).delete(any(Customer.class));
        verify(customerRepository, Mockito.times(1)).delete(customer);


    }
    @Test
    public void testUpdate(){
        Customer customer = new Customer("palagdan", "Daniil", null,"Palagin", "dan.palagin@cvut.cz");
        Mockito.when(customerRepository.findById("palagdan")).thenReturn(Optional.of(customer));
        Mockito.when(customerRepository.findById(not(eq("palagdan")))).thenReturn(Optional.empty());

        customerRepository.save(customer);

        ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);
        Mockito.verify(customerRepository, Mockito.times(1)).save(argumentCaptor.capture());
        Customer customerProvided = argumentCaptor.getValue();
        assertEquals(customer,customerProvided);
    }

}

package com.palagincom.server.ControllersTests;

import com.palagincom.server.Controller.CustomerController;
import com.palagincom.server.business.CustomerService;
import com.palagincom.server.domain.Customer;
import com.palagincom.server.domain.Product;
import com.palagincom.server.exceptions.CustomerNotFound;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.management.InstanceAlreadyExistsException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CustomerController.class)
public class CustomerControllerTests {
    @MockBean
    CustomerService customerService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testCreateExisting() throws Exception {

        doThrow(new InstanceAlreadyExistsException()).when(customerService).create(any(Customer.class));

        Customer customer = new Customer("p");
        mockMvc.perform(post("/v1/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"p\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    public void testFindByName()throws Exception{
        Customer customer1 = new Customer("alana", "Alana", null, "Kaytukova", "alana@mail.cz");
        Customer customer2 = new Customer("alana1", "Alana", null, "Kay", "a@mail.cz");


        List<Customer> customers = List.of(customer1,customer2);
        Mockito.when(customerService.findByName("Alana")).thenReturn(customers);
       // doReturn(Optional.of(customer)).when(customerService.findByName("Alana"));

        //for existing name
        mockMvc.perform(get("/v1/customer/name/Alana"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].name", Matchers.is("Alana")))
                .andExpect(jsonPath("$[1].name", Matchers.is("Alana")));

        //For not existing name
       Mockito.when(customerService.findByName(not(eq("Alana")))).thenReturn(Collections.emptyList());
       // doReturn(Optional.of(customer)).when(customerService.findByName("Alana"));

        mockMvc.perform(get("/v1/customer/name/notalana"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreate() throws Exception {
       Customer customer = new Customer("anna13", "Anna", null, "Palagin", "dan.palagin@mail.cz");

//        Mockito.when(customerService.findByUsername(not(eq("anna13")))).thenReturn(Optional.empty());
//        Mockito.when(customerService.findByUsername("anna13")).thenReturn(Optional.of(customer));

        Mockito.when(customerService.create(any(Customer.class))).thenReturn(customer);


        mockMvc.perform(post("/v1/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"anna13\" ,\"name\":\"Anna\"" +
                        ",\"orders\": null "+
                        ",\"surname\":\"Palagin\"" +
                        ",\"mail\":\"dan.palagin@mail.cz\"}"))
                .andExpect(status().isCreated())
               .andReturn();


        ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);
        Mockito.verify(customerService, Mockito.times(1)).create(argumentCaptor.capture());
        Customer customerProvidedToService = argumentCaptor.getValue();
        assertEquals("anna13", customerProvidedToService.getUsername());
        assertEquals("Anna", customerProvidedToService.getName());
        assertEquals(null, customerProvidedToService.getOrders());
        assertEquals("Palagin", customerProvidedToService.getSurname());
        assertEquals("dan.palagin@mail.cz", customerProvidedToService.getMail());


    }

    @Test
    public void testFindBySurname()throws Exception{
        Customer customer = new Customer("alana", "Alana", null, "Kaytukova", "alana@mail.cz");
        Customer customer1 = new Customer("dana34", "Dana", null, "Kaytukova", "dana@mail.cz");
        List<Customer> customers = List.of(customer,customer1);

        //For existing surname
        Mockito.when(customerService.findBySurname("Kaytukova")).thenReturn(customers);

        mockMvc.perform(get("/v1/customer/surname/Kaytukova"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].surname", Matchers.is("Kaytukova")))
                .andExpect(jsonPath("$[1].surname", Matchers.is("Kaytukova")));

        //For not existing surname
         Mockito.when(customerService.findBySurname(not(eq("Kaytukova")))).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/v1/customer/surname/notKaytukova"))
                .andExpect(status().isNotFound());
    }



    @Test
    public void testUpdateNotExisting() throws Exception {
        Mockito.doThrow(new CustomerNotFound()).when(customerService).update(eq("alana"), any(Customer.class));
        mockMvc.perform(put("/v1/customer/alana")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"alana\"}"))
                .andExpect(status().isNotFound());
    }
    @Test
    public void readAllTests() throws Exception {
        Customer customer = new Customer("bad_customer");
        Customer customer1 = new Customer("cool_customer");
        Customer customer2 = new Customer("neutral_customer");

        List<Customer> customerList = List.of(customer, customer1, customer2);
        Mockito.when(customerService.readAll()).thenReturn(customerList);

        mockMvc.perform(get("/v1/customer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[0].username", Matchers.is("bad_customer")))
                .andExpect(jsonPath("$[1].username", Matchers.is("cool_customer")))
                .andExpect(jsonPath("$[2].username", Matchers.is("neutral_customer")));
    }


    @Test
    public void getOneCustomer() throws Exception {
        Customer customer = new Customer("customer");

        //For existing id
        Mockito.when(customerService.findByUsername("customer")).thenReturn(Optional.of(customer));

        mockMvc.perform(get("/v1/customer/customer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", Matchers.is("customer")));

        //For not existing id
        Mockito.when(customerService.findByUsername(not(eq("customer")))).thenReturn(Optional.empty());

        mockMvc.perform(get("/v1/customer/notcustomer"))
                .andExpect(status().isNotFound());

    }

    @Test
    public void testDelete() throws Exception {

        Customer customer = new Customer("customer");


        Mockito.when(customerService.findByUsername(not(eq("customer")))).thenReturn(Optional.empty());
        Mockito.when(customerService.findByUsername("customer")).thenReturn(Optional.of(customer));


        mockMvc.perform(get("/v1/customer/customer1"))
                .andExpect(status().isNotFound());

        verify(customerService, never()).delete(any());


        mockMvc.perform(delete("/v1/customer/customer"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateCustomer() throws Exception {

        Customer customer = new Customer("anna13", "Anna", null, "Palagin", "dan.palagin@mail.cz");
        Mockito.doNothing().when(customerService).update(eq("anna13"), any(Customer.class));

        mockMvc.perform(put("/v1/customer/anna13")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"anna13\" ,\"name\":\"Anna\"" +
                                ",\"orders\": null "+
                                ",\"surname\":\"Retner\"" +
                                ",\"mail\":\"dan.palagin@mail.cz\"}"))
                .andExpect(status().isNoContent());



        ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);
        Mockito.verify(customerService, Mockito.times(1)).update(eq("anna13"),(argumentCaptor.capture()));
        Customer customerProvidedToService = argumentCaptor.getValue();
        assertEquals("anna13", customerProvidedToService.getUsername());
        assertEquals("Anna", customerProvidedToService.getName());
        assertEquals(null, customerProvidedToService.getOrders());
        assertEquals("Retner", customerProvidedToService.getSurname());
        assertEquals("dan.palagin@mail.cz", customerProvidedToService.getMail());


    }


}

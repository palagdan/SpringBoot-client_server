package com.palagincom.server.ControllersTests;



import com.palagincom.server.Controller.OrderController;

import com.palagincom.server.business.OrderService;

import com.palagincom.server.domain.Category;
import com.palagincom.server.domain.Customer;
import com.palagincom.server.domain.Order;
import com.palagincom.server.domain.Product;
import com.palagincom.server.dto.OrderDTO;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.management.InstanceAlreadyExistsException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)

public class OrderControllerTests {

    @MockBean
    OrderService orderService;

    private final Order  order = new Order();
    private final Order order1 = new Order();
    private final Product  product = new Product();
    private final Customer customer = new Customer();

    OrderControllerTests() {
      product.setId_product(1);
      product.setCategory(Category.Computer);
      product.setDiscount(0.00);
      product.setPrice(29000.00);
      product.setName("Macintosh");

      customer.setUsername("palagdan");
      customer.setName("dan");
      customer.setOrders(null);
      customer.setMail("dan.palagin@cvut.cz");
      customer.setSurname("Palagin");

      List<Product> products = List.of(product);

      order.setId_order(1);
      order.setProducts(products);
      order.setCustomer(customer);
    }




    @Autowired
    MockMvc mockMvc;

    @Test
    public void createTest() throws Exception {
        Mockito.when(orderService.create(any(OrderDTO.class))).thenReturn(order);


        mockMvc.perform(post("/v1/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isCreated())
                .andReturn();


        ArgumentCaptor<OrderDTO> argumentCaptor = ArgumentCaptor.forClass(OrderDTO.class);
        Mockito.verify(orderService, Mockito.times(1)).create(argumentCaptor.capture());
    }

    @Test
    public void readAllTests() throws Exception {
        List<Order> orders = List.of(order);

        Mockito.when(orderService.readAll()).thenReturn(orders);

        mockMvc.perform(get("/v1/order"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].id_order", Matchers.is(1)));

    }

    @Test

    public void getOneOrder() throws Exception {

        //For existing id
        Mockito.when(orderService.findById(1)).thenReturn(Optional.of(order));

        mockMvc.perform(get("/v1/order/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_order", Matchers.is(1)));

        //For not existing id
        Mockito.when(orderService.findById(not(eq(1)))).thenReturn(Optional.empty());

        mockMvc.perform(get("/v1/order/2"))
                .andExpect(status().isNotFound());
    }

    @Test

    public void testDelete() throws Exception {
        Mockito.when(orderService.findById(not(eq(1)))).thenReturn(Optional.empty());
        Mockito.when(orderService.findById(1)).thenReturn(Optional.of(order));

        mockMvc.perform(get("/v1/order/2"))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete("/v1/order/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void update() throws Exception {

        Mockito.doNothing().when(orderService).update(eq(1), any(OrderDTO.class));

        mockMvc.perform(put("/v1/order/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isNoContent())
                .andReturn();


        ArgumentCaptor<OrderDTO> argumentCaptor = ArgumentCaptor.forClass(OrderDTO.class);
        Mockito.verify(orderService, Mockito.times(1)).update(eq(1), argumentCaptor.capture());
        OrderDTO companyProvidedToService = argumentCaptor.getValue();

    }


}

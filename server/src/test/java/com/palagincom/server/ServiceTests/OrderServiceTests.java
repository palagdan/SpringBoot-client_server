package com.palagincom.server.ServiceTests;

import com.palagincom.server.business.OrderService;
import com.palagincom.server.business.ProductService;
import com.palagincom.server.dao.OrderRepository;
import com.palagincom.server.dao.ProductJpaRepository;
import com.palagincom.server.domain.Category;
import com.palagincom.server.domain.Customer;
import com.palagincom.server.domain.Order;
import com.palagincom.server.domain.Product;
import com.palagincom.server.dto.OrderConverter;
import com.palagincom.server.dto.OrderDTO;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.jaxb.SpringDataJaxb;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class OrderServiceTests {
    @InjectMocks
    OrderService orderService;

    @Mock
    OrderRepository orderRepository;


    @Test
    public void testRead(){

        Customer customer = new Customer("palagdan");
        Product product = new Product("Lenovo", Category.Computer,null, 13444.00, 0.00);
        List<Product> products = List.of(product);
        Order order= new Order(customer, products);

        Customer customer1 = new Customer("palagdan");
        Product product1 = new Product("Lenovo234", Category.Computer,null, 13444.00, 0.00);
        List<Product> products1 = List.of(product);
        Order order1= new Order(customer1, products1);

        List<Order> orders = List.of(order,order1);

        Mockito.when(orderService.readAll()).thenReturn(orders);


        Collection<Order> orderCollection = orderService.readAll();

        assertEquals(2,orderCollection.size());
        verify(orderRepository, times(1)).findAll();

    }

    @Test
    @Disabled
    public void testCreate() throws Exception {
        Customer customer = new Customer("palagdan");
        Product product = new Product("Lenovo", Category.Computer,null, 13444.00, 0.00);
        List<Product> products = List.of(product);
        List<Integer> productsList = List.of(product.getId_product());
        OrderDTO order= new OrderDTO();
        Order order1 = new Order(customer,products);
        order.setId_order(1);
        order.setCustomer(customer.getUsername());
        order.setProducts(productsList);

        Mockito.when(orderRepository.save(any(Order.class))).thenReturn(order1);
        Order created = orderService.create(order);


        ArgumentCaptor<Order> argumentCaptor = ArgumentCaptor.forClass(Order.class);
        Mockito.verify(orderRepository, Mockito.times(1)).save(argumentCaptor.capture());

        assertEquals(created,order1);


    }

    @Test
    public void readOneTest(){
        Customer customer = new Customer("palagdan");
        Product product = new Product("Lenovo", Category.Computer,null, 13444.00, 0.00);
        List<Product> products = List.of(product);
        Order order= new Order(customer, products);


        Mockito.when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        Mockito.when(orderRepository.findById(not(eq(1)))).thenReturn(Optional.empty());

        Order orderFound = orderService.findById(1).get();
        assertEquals(orderFound,order);
    }

    @Test
    public void deleteTest() throws Exception {
        Customer customer = new Customer("palagdan");
        Product product = new Product("Lenovo", Category.Computer,null, 13444.00, 0.00);
        List<Product> products = List.of(product);
        Order order= new Order(customer, products);

        Mockito.when((orderRepository.findById(1))).thenReturn(Optional.of(order));

        Mockito.doNothing().when(orderRepository).delete(order);

        orderService.delete(1);

        Mockito.verify(orderRepository,Mockito.times(1)).delete(order);
    }

    @Test
    public void updateTest(){
        Customer customer = new Customer("palagdan");
        Product product = new Product("Lenovo", Category.Computer,null, 13444.00, 0.00);
        List<Product> products = List.of(product);
        Order order= new Order(customer, products);


        Mockito.when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        Mockito.when(orderRepository.findById(not(eq(1)))).thenReturn(Optional.empty());

        orderRepository.save(order);

        ArgumentCaptor<Order> argumentCaptor = ArgumentCaptor.forClass(Order.class);
        Mockito.verify(orderRepository, Mockito.times(1)).save(argumentCaptor.capture());
        Order orderProvided = argumentCaptor.getValue();
        assertEquals(order,orderProvided);
    }
}

package com.palagincom.server.business;

import com.palagincom.server.dao.OrderRepository;
import com.palagincom.server.domain.Customer;
import com.palagincom.server.domain.Order;
import com.palagincom.server.domain.Product;
import com.palagincom.server.dto.OrderConverter;
import com.palagincom.server.dto.OrderDTO;
import com.palagincom.server.exceptions.OrderNotFound;
import com.palagincom.server.exceptions.ProductDoesNotExist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.management.InstanceAlreadyExistsException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService{

   private final  OrderRepository orderRepository;
   private final CustomerService customerService;
   private final ProductService productService;


    @Autowired
    public OrderService(OrderRepository orderRepository, CustomerService customerService, ProductService productService) {
        this.orderRepository = orderRepository;
        this.customerService = customerService;
        this.productService = productService;
    }


    @Transactional
    public Order create(OrderDTO order) throws Exception {
        List<Product> products = new ArrayList<>();
        for (int p : order.getId()) {
            products.add(productService.findById(p).get());
        }
        if(products.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);


        Optional<Customer> customer = customerService.findByUsername(order.getCustomer());
        if(customer.isEmpty())  throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        Order order1 = OrderConverter.fromDto(order, products,customer.get());

        if (orderRepository.findById(order.getId_order()).isPresent())
            throw new InstanceAlreadyExistsException("Order with id: " + order.getId_order() + " already exists");
        return orderRepository.save(order1);
    }
    @Transactional
     public void update( int id, OrderDTO order)throws Exception{

        List<Product> products = new ArrayList<>();
        for (int p : order.getId()) {
            products.add(productService.findById(p).get());
        }
        if(products.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);


        Optional<Customer> customer = customerService.findByUsername(order.getCustomer());
        if(customer.isEmpty())  throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        Order order1 = OrderConverter.fromDto(order, products,customer.get());

        Optional<Order> find =orderRepository.findById(id);

        if(find.isEmpty()) throw new ProductDoesNotExist("Order with id " + order.getId_order() + " not found");

        orderRepository.save(order1);
    }

    @Transactional
    public void delete(int id) throws Exception{
        Optional<Order> order = findById(id);
        if(order.isEmpty())
            throw new OrderNotFound("Order  with id " + id + " not found.");
        orderRepository.delete(order.get());
    }

    public List<Order> readAll() {
        return orderRepository.findAll();
    }

    public Optional<Order> findById(int id) {
        return orderRepository.findById(id);
    }


}


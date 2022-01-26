package com.palagincom.server.dto;

import com.palagincom.server.domain.Customer;
import com.palagincom.server.domain.Order;
import com.palagincom.server.domain.Product;

import java.util.List;
import java.util.Optional;

public class OrderConverter {
    public static Order fromDto(OrderDTO dto, List<Product> products, Customer customer) {
        Order order = new Order();
        order.setId_order(dto.getId_order());
        order.setProducts(products);
        order.setCustomer(customer);
        return order;
    }
}

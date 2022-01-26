
package com.palagincom.server.Controller;


import com.palagincom.server.business.CustomerService;
import com.palagincom.server.business.OrderService;
import com.palagincom.server.business.ProductService;
import com.palagincom.server.domain.Customer;
import com.palagincom.server.domain.Order;
import com.palagincom.server.domain.Product;
import com.palagincom.server.dto.OrderConverter;
import com.palagincom.server.dto.OrderDTO;
import com.palagincom.server.exceptions.CustomerNotFound;
import com.palagincom.server.exceptions.OrderNotFound;
import com.palagincom.server.exceptions.ProductDoesNotExist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.management.InstanceAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1")
public class OrderController {

    private final  OrderService orderService;


    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;

    }


    @PostMapping("/order")
    public ResponseEntity<Order> addOrder(@RequestBody OrderDTO dto)throws Exception{

        try {
            Order orderCreated =  orderService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(orderCreated);
        }catch(InstanceAlreadyExistsException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/order")
    List<Order> findAllOrders(){
        return orderService.readAll();
    }

    @GetMapping("/order/{id}")
    public Order productFindById(@PathVariable int id){
        return orderService.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
    }




    @DeleteMapping("/order/{id}")
    public void deleteById( @PathVariable int  id)throws Exception{
        try{
            orderService.delete(id);
        }catch(OrderNotFound e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/order/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void  updateOrder( @PathVariable int  id,@RequestBody OrderDTO dto)throws Exception{
        try {
            orderService.update(id,dto);
        }catch(InstanceAlreadyExistsException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

    }

}
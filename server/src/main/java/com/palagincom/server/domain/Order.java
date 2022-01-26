package com.palagincom.server.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Entity(name = "order1")
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id_order;

    @ManyToMany()
    @JoinTable(name = "product1_orders"
            , joinColumns = @JoinColumn(name = "orders_id_order")
            ,inverseJoinColumns = @JoinColumn(name = "products_id_product"))
    private List<Product> products;

    @ManyToOne()
    @JsonBackReference
    private Customer customer;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private final LocalDateTime time = LocalDateTime.now();

    public Order( Customer customer ,List<Product> product) {
        this.products = product;

        this.customer = customer;
    }

    public Order(){

    }


    public int getId_order() {
        return id_order;
    }

    public void setId_order(int id_order) {
        this.id_order = id_order;
    }

    public List<Product> getProducts() {
        return products;
    }
    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public LocalDateTime getTime() {
        return time;
    }



}

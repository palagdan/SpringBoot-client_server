package com.palagincom.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity(name = "product1")
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id_product;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.ORDINAL)
    Category category;

    @JsonIgnore
    @ManyToMany(mappedBy = "products")
    List<Order> orders;


    private Double price;


    private Double discount;


    public Product( String  name, Category category,List<Order> orders,Double price, Double discount) {
        this.name = name;
        this.category = category;
        this.orders = orders;
        this.price = price;
        this.discount = discount;
    }

    public Product(String name) {
        this.name = name;
    }

    public Product() {

    }

    public void setId_product(int id_product) {
        this.id_product = id_product;
    }

    public int getId_product() {
        return id_product;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id_product=" + id_product +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", discount=" + discount +
                '}';
    }


}

package com.palagincom.server.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity(name = "customer1")
public class Customer implements Serializable {


    @Id
    private String username;

    private String name;

    private String surname;

    private String mail;



   @OneToMany(mappedBy = "customer")
   @JsonManagedReference
    List<Order> orders;

    public Customer(String username, String name, List<Order> orders,String surname, String mail) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.orders = orders;
        this.mail = mail;
    }
    public Customer(String username) {
        this.username = username;
    }


    public Customer() {

    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id_customer=" + username +
                ", name='" + name + '\'' +
                ", mail='" + mail + '\'' +
                '}';
    }
}

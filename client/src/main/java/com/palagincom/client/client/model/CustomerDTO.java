package com.palagincom.client.client.model;

import java.io.Serializable;

public class CustomerDTO {


        private String username;

        private String name;

        private String surname;

        private String mail;

        public CustomerDTO(String username, String name, String surname,String mail) {
            this.username = username;
            this.name = name;
            this.surname = surname;
            this.mail = mail;
        }

        public CustomerDTO() {
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}

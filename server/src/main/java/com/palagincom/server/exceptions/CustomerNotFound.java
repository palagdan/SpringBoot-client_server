package com.palagincom.server.exceptions;

public class CustomerNotFound extends Exception{

    public CustomerNotFound(String error) {
        super(error);
    }

    public CustomerNotFound() {

    }
}

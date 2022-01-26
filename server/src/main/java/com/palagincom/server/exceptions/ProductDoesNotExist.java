package com.palagincom.server.exceptions;

public class ProductDoesNotExist extends Exception {
    public ProductDoesNotExist(String error) {
        super(error);
    }

    public ProductDoesNotExist() {

    }
}

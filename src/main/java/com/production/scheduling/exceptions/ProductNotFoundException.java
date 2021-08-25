package com.production.scheduling.exceptions;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("Cannot find product with id: " + id);
    }
}

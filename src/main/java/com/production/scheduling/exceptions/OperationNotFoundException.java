package com.production.scheduling.exceptions;

public class OperationNotFoundException extends RuntimeException {
    public OperationNotFoundException(Long id) {
        super("Operation not found at id: " + id);
    }
}

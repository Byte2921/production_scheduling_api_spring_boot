package com.production.scheduling.exceptions;

public class WorkplaceNotFoundException extends RuntimeException {
    public WorkplaceNotFoundException(Long id){
        super("Workplace not found at id:" + id);
    }
}

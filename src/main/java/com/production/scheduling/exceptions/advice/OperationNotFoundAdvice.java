package com.production.scheduling.exceptions.advice;

import com.production.scheduling.exceptions.OperationNotFoundException;
import com.production.scheduling.exceptions.WorkplaceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class OperationNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(OperationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String productNotFoundHandler(OperationNotFoundException ex) {
        return ex.getMessage();
    }
}

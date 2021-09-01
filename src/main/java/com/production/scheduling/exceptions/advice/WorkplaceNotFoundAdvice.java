package com.production.scheduling.exceptions.advice;

import com.production.scheduling.exceptions.ProductNotFoundException;
import com.production.scheduling.exceptions.WorkplaceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class WorkplaceNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(WorkplaceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String productNotFoundHandler(WorkplaceNotFoundException ex) {
        return ex.getMessage();
    }
}

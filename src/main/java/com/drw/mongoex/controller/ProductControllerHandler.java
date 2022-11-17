package com.drw.mongoex.controller;

import com.drw.mongoex.dto.ErrorResponse;
import com.drw.mongoex.util.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ProductControllerHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> resourceException(Exception e){
        return ResponseHandler.generateResponse("An Exception Occurred", HttpStatus.INTERNAL_SERVER_ERROR,
                new ErrorResponse(e.getClass().getSimpleName(), e.getMessage()));
    }
}

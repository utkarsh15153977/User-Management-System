package com.user.usermanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String>  handleUserNotFound(UserNotFoundException exception){
        ErrorDetails error = new ErrorDetails(
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(error.toString(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistingException.class)
    public ResponseEntity<String> handleUserAlreadyExists(UserAlreadyExistingException exception){
        ErrorDetails error = new ErrorDetails(
                HttpStatus.CONFLICT.value(),
                exception.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(error.toString(), HttpStatus.CONFLICT);
    }
}
package com.user.usermanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistingException.class)
    public ResponseEntity<String> handleUserAlreadyExists(UserAlreadyExistingException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    // Handling SMTP/Mail Failures
    @ExceptionHandler(org.springframework.mail.MailException.class)
    public ResponseEntity<Map<String, String>> handleMailException(org.springframework.mail.MailException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", "Email Service Unavailable");
        errorMap.put("details", "The user was created, but we couldn't send the welcome email. Please check your inbox later.");
        return new ResponseEntity<>(errorMap, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
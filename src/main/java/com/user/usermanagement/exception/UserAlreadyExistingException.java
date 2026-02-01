package com.user.usermanagement.exception;

public class UserAlreadyExistingException extends RuntimeException{
    public UserAlreadyExistingException(String msg){
        super(msg);
    }
}
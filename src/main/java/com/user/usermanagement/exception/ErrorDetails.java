package com.user.usermanagement.exception;

import lombok.Data;

@Data
public class ErrorDetails {
    private int status;

    public ErrorDetails(int status, String message, long timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    private String message;
    private long timestamp;
}

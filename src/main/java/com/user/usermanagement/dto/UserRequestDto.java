package com.user.usermanagement.dto;

import lombok.Data;

@Data
public class UserRequestDto {
    private String name;
    private String username;
    private String password;
    private String email;
    private String mobileNumber;
}

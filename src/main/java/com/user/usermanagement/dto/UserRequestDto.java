package com.user.usermanagement.dto;

import lombok.Data;

@Data
public class UserRequestDto {
    public Object requestDtos;
    private String name;
    private String username;
    private String password;
    private String email;
    private String mobileNumber;
}

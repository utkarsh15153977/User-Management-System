package com.user.usermanagement.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String username;
    private String password;
}

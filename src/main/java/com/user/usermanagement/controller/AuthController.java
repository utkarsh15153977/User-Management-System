package com.user.usermanagement.controller;

import com.user.usermanagement.dto.LoginRequestDto;
import com.user.usermanagement.dto.LoginResponseDto;
import com.user.usermanagement.entity.User;
import com.user.usermanagement.repository.UserRepository;
import com.user.usermanagement.security.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth/login")
@Data
public class AuthController {
    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil,@Lazy AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    @PostMapping()
    public LoginResponseDto login(@RequestBody LoginRequestDto loginRequestDto){

        //Authenticate Username and Password
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequestDto.getUsername(),
                loginRequestDto.getPassword()));

        //Save username and password in DB.
        User user = userRepository.findByUsername(loginRequestDto.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        //Generate Jwt Token
        String token = jwtUtil.generatedToken(user.getUsername(), user.getRole().name());

        return new LoginResponseDto(token);
    }
}

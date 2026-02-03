package com.user.usermanagement.controller;

import com.user.usermanagement.dto.LoginRequestDto;
import com.user.usermanagement.dto.LoginResponseDto;
import com.user.usermanagement.entity.User;
import com.user.usermanagement.repository.UserRepository;
import com.user.usermanagement.security.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@Data
@Slf4j
@AllArgsConstructor
public class AuthController {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    @PostMapping("login")
    public LoginResponseDto login(@RequestBody LoginRequestDto loginRequestDto){

        try{
            //Authenticate Username and Password
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequestDto.getUsername(),
                    loginRequestDto.getPassword()));
        }
        catch(BadCredentialsException e){
            System.out.println("Bad credentials");
            log.info("Exception throwing {}", e.getMessage());
        }
        //Save username and password in DB.
        User user = userRepository.findByUsername(loginRequestDto.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        //Generate Jwt Token
        String token = jwtUtil.generatedToken(user.getUsername(), user.getRole().name());

        return new LoginResponseDto(token);
    }
}

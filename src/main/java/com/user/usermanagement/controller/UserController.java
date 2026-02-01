package com.user.usermanagement.controller;

import com.user.usermanagement.dto.UserRequestDto;
import com.user.usermanagement.dto.UserResponseDto;
import com.user.usermanagement.entity.User;
import com.user.usermanagement.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    //Create User
    @PostMapping("/createUser")
    public ResponseEntity<UserResponseDto> createUser(
            @RequestBody UserRequestDto requestDto) {
        log.info("UserController.createUser");

        User user = mapToEntity(requestDto);
        log.info("User : {}", user);
        User savedUser = userService.addUser(user);
        log.info("User : {}", savedUser);

        return new ResponseEntity<>(
                mapToResponseDto(savedUser),
                HttpStatus.CREATED
        );
    }

    //Get User by ID
    @GetMapping("/fetchUser/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {

        User user = userService.getUserById(id);
        log.info("User : {}", user);
        return ResponseEntity.ok(mapToResponseDto(user));
    }

    //Get All Users
    @GetMapping("/fetchAllUsers")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {

        List<UserResponseDto> list =
                userService.getAllUser()
                        .stream()
                        .map(this::mapToResponseDto)
                        .collect(Collectors.toList());

        log.info("User : {}", list);

        return ResponseEntity.ok(list);
    }

    //-------- MAPPER METHODS --------

    private User mapToEntity(UserRequestDto dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setMobileNumber(dto.getMobileNumber());
        return user;
    }

    private UserResponseDto mapToResponseDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getEmail(),
                user.getMobileNumber(),
                user.getRole()
        );
    }
}
package com.user.usermanagement.controller;

import com.user.usermanagement.dto.UserRequestDto;
import com.user.usermanagement.dto.UserResponseDto;
import com.user.usermanagement.entity.User;
import com.user.usermanagement.security.JwtUtil;
import com.user.usermanagement.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {


    private final UserService userService;
    private final JwtUtil jwtUtil;
    public UserController(PasswordEncoder passwordEncoder, UserService userService,  JwtUtil jwtUtil) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    private final PasswordEncoder passwordEncoder;
    //Dependency Injection


    //Create User
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/createUser")
    public ResponseEntity<UserResponseDto> createUser(
            @RequestBody UserRequestDto requestDto) {
        log.info("UserController.createUser");
        User user = mapToEntity(requestDto);
        //log.info("User : {}", user);
        //String rawPassword = user.getPassword();
        //log.info("rawPassword : {}", rawPassword);
        //String encodedPassword = passwordEncoder.encode(rawPassword);
        //log.info("encodedPassword : {}", encodedPassword);
        //user.setPassword(encodedPassword);
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

    @PostMapping("/updateUserById/{id}")
    public ResponseEntity<UserResponseDto> updateUserDetailsById(@PathVariable Long id, @RequestBody UserRequestDto requestDto) {
        //Fetching existing user and update all data.
        User userRequest = new User();
        userRequest.setId(id);
        userRequest.setName(requestDto.getName());
        userRequest.setMobileNumber(requestDto.getMobileNumber());
        userRequest.setEmail(requestDto.getEmail());
        userRequest.setPassword(requestDto.getPassword());
        userRequest.setUsername(requestDto.getUsername());
        log.info("User : {}", userRequest);

        userService.updateUser(userRequest);
        log.info("User : {}", userRequest);

        // Fetch the updated user to get the latest data
        User updatedUser = userService.getUserById(id);
        log.info("User : {}", updatedUser);

        //Generate a new JWT Token for updating new details from the old details.
        String newToken = jwtUtil.generatedToken(updatedUser.getUsername(), String.valueOf(updatedUser.getRole()));
        log.info("New Token : {}", newToken);

        return ResponseEntity.ok()
                .header("Authorization", "Bearer", newToken)
                .body(mapToResponseDto(updatedUser));
    }


    // 2. USER can only view their own profile
    // #username refers to the path variable, principal.username is the logged-in user
    @PreAuthorize("hasRole('ADMIN') or #username == principal.username")
    @GetMapping("/profile/{username}")
    public ResponseEntity<UserResponseDto> getUserProfile(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        log.info("User : {}", user);
        return ResponseEntity.ok(mapToResponseDto(user));
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
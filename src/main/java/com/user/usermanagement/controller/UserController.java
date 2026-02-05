package com.user.usermanagement.controller;

import com.user.usermanagement.dto.UserRequestDto;
import com.user.usermanagement.dto.UserResponseDto;
import com.user.usermanagement.entity.User;
import com.user.usermanagement.repository.UserRepository;
import com.user.usermanagement.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;


    //Create User
    @PostMapping("/createUser")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto requestDto) {
        log.info("UserController.createUser");
        User user = mapToEntity(requestDto);
        User savedUser = userService.addUser(user);
        log.info("User : {}", savedUser);
        return new ResponseEntity<>(mapToResponseDto(savedUser), HttpStatus.CREATED);
    }

    //Update User
    @PutMapping("/updateUserById/{id}")
    public ResponseEntity<UserResponseDto> updateUserById(@RequestBody UserRequestDto requestDto, @PathVariable Long id) {
        log.info("UserController.updateUserById");
        User user = userRepository.findById(id).
                orElseThrow(() -> new UsernameNotFoundException("User Id not found, enter which is existing."));

        //Update all the fields
        user.setName(requestDto.getName());
        user.setEmail(requestDto.getEmail());
        user.setUsername(requestDto.getUsername());
        user.setMobileNumber(requestDto.getMobileNumber());

        if(requestDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        }

        userRepository.save(user);
        return  new ResponseEntity<>(
                mapToResponseDto(user),
                HttpStatus.OK
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

    //Delete User by Id
    @DeleteMapping("/removeUser/{id}")
    public ResponseEntity<UserResponseDto> removeUserById(@PathVariable Long id) {
        log.info("UserController.removeUserById");
        userRepository.deleteById(id);
        return ResponseEntity.ok(mapToResponseDto(userService.getUserById(id)));
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
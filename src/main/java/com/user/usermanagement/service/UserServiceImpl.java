package com.user.usermanagement.service;

import com.user.usermanagement.dto.UserRequestDto;
import com.user.usermanagement.entity.Role;
import com.user.usermanagement.entity.User;
import com.user.usermanagement.exception.UserAlreadyExistingException;
import com.user.usermanagement.exception.UserNotFoundException;
import com.user.usermanagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    public User addUser(User user) throws UserAlreadyExistingException {

        log.info("Attempting to add user with username: {}", user.getUsername());
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistingException("Email already exists");
        }

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UserAlreadyExistingException("Username already exists");
        }
        // Password Encoder
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        //Assign default roles.
        if(user.getRole() == null){
            user.setRole(Role.ADMIN);
            log.info("Assigned default role for USER to user: {}", user.getUsername());
        }
        User savedUser = userRepository.save(user);
        log.info("Triggering welcome email for: {}", savedUser.getEmail());
        emailService.sendWelcomeEmail(savedUser.getEmail(), savedUser.getName());
        return userRepository.save(user);
    }
    @Override
    public String updateUser(User user) throws UserNotFoundException {
        log.info("Attempting to update user ID: {}", user.getId());
        User existingUser = userRepository.findById(user.getId()).orElseThrow(() ->{
            log.info("User not found with ID: {}", user.getId());
            return new UserNotFoundException("User not found with ID: " + user.getId());
        });
        existingUser.setName(user.getName());
        existingUser.setMobileNumber(user.getMobileNumber());
        existingUser.setEmail(user.getEmail());

        if(user.getPassword() != null && !user.getPassword().isEmpty()){
            log.info("Changing password for user with ID: {}", user.getId());
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(existingUser);
        log.info("User successfully updated with ID: {}", user.getId());
        return "The user got updated.";
    }

    @Override
    public User getUserById(Long id) throws UserNotFoundException {
        log.info("Attempting to get user with ID: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not exists for id: " +id));
    }

    @Override
    public List<User> getAllUser(){
        log.info("Attempting to get all users");
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long id) throws UserNotFoundException {
        log.warn("Attempting to delete user ID: {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found for this id."));
        userRepository.delete(user);
        log.info("User successfully deleted with ID: {}", id);
    }


    @Transactional(rollbackOn = Exception.class)
    @Override
    public List<User> saveAllUsers(List<UserRequestDto> dtos){
        log.info("Attempting to save all users");
        if(dtos == null || dtos.isEmpty()){
            return new ArrayList<>();
        }

        Set<String> emails = new HashSet<>();
        Set<String> usernames = new HashSet<>();
        List<User> users = new ArrayList<>();

        for(UserRequestDto dto : dtos){
            String email = dto.getEmail();
            String username = dto.getUsername();
            if(email != null && !emails.add(email)){
                throw new UserAlreadyExistingException("Email already exists");
            }
            if(username != null && !usernames.add(username)){
                throw new UserAlreadyExistingException("Username already exists");
            }

            if(email != null && userRepository.findByEmail(email).isPresent()){
                throw new UserAlreadyExistingException("Email already exists");
            }

            if(username != null && userRepository.findByUsername(username).isPresent()){
                throw new UserAlreadyExistingException("Username already exists");
            }

            User user = new User();
            user.setEmail(email);
            user.setUsername(username);
            user.setName(username);
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            if(user.getRole() == null){
                user.setRole(Role.USER);
            }
            users.add(user);
        }
        return userRepository.saveAll(users);
    }

}

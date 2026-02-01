package com.user.usermanagement.service;

import com.user.usermanagement.entity.Role;
import com.user.usermanagement.entity.User;
import com.user.usermanagement.exception.UserAlreadyExistingException;
import com.user.usermanagement.exception.UserNotFoundException;
import com.user.usermanagement.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
            user.setRole(Role.USER);
            log.info("Assigned default role for USER to user: {}", user.getUsername());
        }

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
}

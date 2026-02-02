package com.user.usermanagement.service;

import com.user.usermanagement.entity.User;
import com.user.usermanagement.exception.UserAlreadyExistingException;
import com.user.usermanagement.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {

    User addUser(User user) throws UserAlreadyExistingException;
    String updateUser(User user) throws UserNotFoundException;
    void deleteUser(Long id) throws UserNotFoundException;
    User getUserById(Long id) throws UserNotFoundException;
    List<User> getAllUser();
    //User loadUserByUsername(String username);
    User getUserByUsername(String username);
}

package com.user.usermanagement.security;

import com.user.usermanagement.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailsService {
    UserDetails loadUserByUsername(String username);
}

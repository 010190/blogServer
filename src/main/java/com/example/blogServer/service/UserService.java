package com.example.blogServer.service;

import com.example.blogServer.dto.UserRegistrationDto;
import com.example.blogServer.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User registerNewUser(UserRegistrationDto registrationDto) throws Exception;
    boolean isUsernameAvailable(String username);
    User findByUsername(String username);

    Long findUserIdByUsername(String username);
}


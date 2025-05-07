package com.example.blogServer.service;

import com.example.blogServer.entity.Post;
import com.example.blogServer.entity.User;
import com.example.blogServer.entity.UserRole;
import com.example.blogServer.repository.PostRepository;
import com.example.blogServer.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Użytkownik o takiej nazwie już istnieje.");
        }
        if (user.getRole() == null) {
            user.setRole(UserRole.READER);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public boolean canCreatePost(User user) {
        UserRole role = user.getRole();
        return role == UserRole.ADMIN || role == UserRole.CONTENT_CREATOR;
    }

    @Override
    public boolean canModerateComment(User user) {
        return user.getRole() == UserRole.ADMIN;
    }

    @Override
    public List<Post> getUserPosts(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Użytkownik nie znaleziony! id=" + userId));
        return postRepository.findByPostedBy(userId);
    }
}



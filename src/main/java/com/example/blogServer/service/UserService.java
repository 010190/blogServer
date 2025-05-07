package com.example.blogServer.service;

import com.example.blogServer.entity.Post;
import com.example.blogServer.entity.User;

import java.util.List;

public interface UserService {
    User registerUser(User user);

    boolean canCreatePost(User user);

    boolean canModerateComment(User user);

    List<Post> getUserPosts(Long userId);
}


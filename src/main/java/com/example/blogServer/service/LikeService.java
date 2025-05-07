package com.example.blogServer.service;

public interface LikeService {
    void likePost(Long postId, Long userId);
    long countLikes(Long postId);
}


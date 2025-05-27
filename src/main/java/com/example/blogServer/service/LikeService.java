package com.example.blogServer.service;

public interface LikeService {
    void likePost(Long postId, Long userId);
    long countLikes(Long postId);

    boolean hasUserLikedPost(Long postId, Long userId);
    void unlikePost(Long postId, Long userId);

    void removeLikesByPostId(Long postId);


}


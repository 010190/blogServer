package com.example.blogServer.service;

import com.example.blogServer.entity.Post;

import java.util.List;

public interface PostService {
    Post savePost(Post post);

    List<Post> getAllPosts();

    Post getPostById(Long postId);

    void likePost(Long postId, Long userId);

    List<Post> searchByTitle(String title);

    List<Post> getRecentPosts(int limit);

    List<Post> getMostLikedPosts(int limit);

    int getTotalUserLikes(Long userId);

    List<Post> getUserPosts(Long userId);

    boolean deletePost(Long postId, Long userId);

    void addTagsToPost(Long postId, List<String> tagNames);

    Post updatePost(Post post);

    void updateTagsForPost(Long postId, List<String> tagNames);

    List<Post> searchByTagTitle(String tagName);


    List<Post> getUserPostsByAuthor(String authorUsername);




}

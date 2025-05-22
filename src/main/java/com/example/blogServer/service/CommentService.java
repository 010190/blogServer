package com.example.blogServer.service;

import com.example.blogServer.entity.Comment;

import java.util.List;

public interface CommentService {
    Comment createComment(Long postId, Long userId, String content);

    List<Comment> getCommentsByPostId(Long postId);
    void deleteByPostId(Long postId);


    boolean deleteComment(Long commentId, Long userId);

}

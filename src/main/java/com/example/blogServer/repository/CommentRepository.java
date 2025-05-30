package com.example.blogServer.repository;

import com.example.blogServer.entity.Comment;
import com.example.blogServer.service.CommentService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPostId(Long postId);

    void deleteByPostId(Long postId);

    int countByPostId(Long postId);


}

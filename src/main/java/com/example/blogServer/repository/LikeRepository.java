package com.example.blogServer.repository;

import com.example.blogServer.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {
    long countByPostId(Long postId);
    List<Like> findByPostId(Long postId);
}

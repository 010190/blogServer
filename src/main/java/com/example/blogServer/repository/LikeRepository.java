package com.example.blogServer.repository;

import com.example.blogServer.entity.Like;
import com.example.blogServer.entity.Post;
import com.example.blogServer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    long countByPostId(Long postId);
    List<Like> findByPostId(Long postId);
    boolean existsByPostIdAndUserId(Long postId, Long userId);

    Optional<Like> findByPostAndUser(Post post, User user);

}

package com.example.blogServer.repository;

import com.example.blogServer.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByTitleContainingIgnoreCase(String title);
    Page<Post> findAll(Pageable pageable);
    List<Post> findByPostedBy(Long postedBy);
    List<Post> findByTags_Title(String title);
    List<Post> findAllByAuthor(String author);



}


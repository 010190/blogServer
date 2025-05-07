package com.example.blogServer.entity;

import jakarta.persistence.*;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
public class Statistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private Long postId;
    @Setter
    private int views;
    @Setter
    private int likes;
    @Setter
    private int comments;
    @Setter
    private LocalDateTime date = LocalDateTime.now();

    public Statistics() {}
    public Statistics(Long postId, int views, int likes, int comments) {
        this.postId = postId;
        this.views = views;
        this.likes = likes;
        this.comments = comments;
    }

    public Long getId() { return id; }
    public Long getPostId() { return postId; }

    public int getViews() { return views; }

    public int getLikes() { return likes; }

    public int getComments() { return comments; }

    public LocalDateTime getDate() { return date; }
}



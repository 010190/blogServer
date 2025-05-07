package com.example.blogServer.entity;

import jakarta.persistence.*;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "post_likes")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(optional = false)
    private User user;

    @Setter
    @ManyToOne(optional = false)
    private Post post;

    @Setter
    private LocalDateTime createdAt = LocalDateTime.now();

    public Like() {}

    public Like(User user, Post post) {
        this.user = user;
        this.post = post;
    }

    public Long getId() { return id; }
    public User getUser() { return user; }

    public Post getPost() { return post; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}


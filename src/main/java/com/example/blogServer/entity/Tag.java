package com.example.blogServer.entity;

import jakarta.persistence.*;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String title;

    @Setter
    private String subtitle;


    @Setter
    @ManyToMany(mappedBy = "tags")
    private Set<Post> posts = new HashSet<>();

    public Tag() {}
    public Tag(String title) { this.title = title; }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getSubtitle() { return subtitle; }


    public Set<Post> getPosts() { return posts; }
}


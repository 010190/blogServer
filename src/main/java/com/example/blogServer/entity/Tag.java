package com.example.blogServer.entity;

import jakarta.persistence.*;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String name;

    @Setter
    @ManyToMany(mappedBy = "tags")
    private Set<Post> posts = new HashSet<>();

    public Tag() {}
    public Tag(String name) { this.name = name; }

    public Long getId() { return id; }
    public String getName() { return name; }

    public Set<Post> getPosts() { return posts; }
}


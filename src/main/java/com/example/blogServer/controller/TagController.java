package com.example.blogServer.controller;

import com.example.blogServer.entity.Post;
import com.example.blogServer.entity.Tag;
import com.example.blogServer.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    @Autowired private TagService tagService;

    @GetMapping
    public List<Tag> all() {
        return tagService.getAllTags();
    }

    @PostMapping
    public Tag create(@RequestBody Tag t) {
        return tagService.createTag(t.getName());
    }

    @GetMapping("/{id}/posts")
    public List<Post> posts(@PathVariable Long id) {
        return tagService.getPostsByTag(id);
    }
}


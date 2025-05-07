package com.example.blogServer.controller;

import com.example.blogServer.entity.Post;
import com.example.blogServer.service.PostService;
import com.example.blogServer.service.StatisticsService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final StatisticsService statisticsService;


    @Autowired
    public PostController(PostService postService,
                          StatisticsService statisticsService) {
        this.postService = postService;
        this.statisticsService = statisticsService;
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        Post created = postService.savePost(post);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable Long postId) {
        try {
            Post post = postService.getPostById(postId);
            return ResponseEntity.ok(post);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        }
    }

    @PutMapping("/{postId}/like")
    public ResponseEntity<?> likePost(
            @PathVariable Long postId,
            @RequestParam("userId") Long userId
    ) {
        try {
            postService.likePost(postId, userId);
            return ResponseEntity.ok("Post liked successfully.");
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        }
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<List<Post>> searchByName(@PathVariable String name) {
        return ResponseEntity.ok(postService.searchByName(name));
    }

    @GetMapping("/recent")
    public ResponseEntity<List<Post>> getRecentPosts(
            @RequestParam(defaultValue = "10") int limit
    ) {
        return ResponseEntity.ok(postService.getRecentPosts(limit));
    }

    @GetMapping("/most-liked")
    public ResponseEntity<List<Post>> getMostLikedPosts(
            @RequestParam(defaultValue = "10") int limit
    ) {
        return ResponseEntity.ok(postService.getMostLikedPosts(limit));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Post>> getUserPosts(@PathVariable Long userId) {
        return ResponseEntity.ok(postService.getUserPosts(userId));
    }

    @GetMapping("/user/{userId}/likes")
    public ResponseEntity<Integer> getTotalUserLikes(@PathVariable Long userId) {
        return ResponseEntity.ok(postService.getTotalUserLikes(userId));
    }
}



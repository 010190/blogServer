package com.example.blogServer.controller;

import com.example.blogServer.service.LikeService;
import com.example.blogServer.service.PostService;
import com.example.blogServer.service.StatisticsService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/post/{postId}/likes")
public class LikeController {

    private final LikeService likeService;
    private final PostService postService;
    private final StatisticsService statisticsService;

    @Autowired
    public LikeController(LikeService likeService,
                          PostService postService,
                          StatisticsService statisticsService) {
        this.likeService = likeService;
        this.postService = postService;
        this.statisticsService = statisticsService;
    }

    @PostMapping
    public ResponseEntity<?> like(
            @PathVariable Long postId,
            @RequestParam("userId") Long userId
    ) {
        try {
            likeService.likePost(postId, userId);
            postService.likePost(postId, userId);
            statisticsService.recordLike(postId);
            return ResponseEntity.ok("Post liked successfully.");
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count(@PathVariable Long postId) {
        long total = likeService.countLikes(postId);
        return ResponseEntity.ok(total);
    }
}



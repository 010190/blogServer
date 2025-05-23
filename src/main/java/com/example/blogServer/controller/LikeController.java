package com.example.blogServer.controller;

import com.example.blogServer.entity.User;
import com.example.blogServer.service.LikeService;
import com.example.blogServer.service.PostService;
import com.example.blogServer.service.StatisticsService;
import com.example.blogServer.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/api/post/{postId}/likes")
public class LikeController {

    private final LikeService likeService;
    private final PostService postService;
    private final StatisticsService statisticsService;

    private final UserService userService;

    @Autowired
    public LikeController(LikeService likeService,
                          PostService postService,
                          StatisticsService statisticsService, UserService userService) {
        this.likeService = likeService;
        this.postService = postService;
        this.statisticsService = statisticsService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> like(@PathVariable Long postId, Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = authentication.getName();
        User user = userService.findByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Użytkownik nie istnieje");
        }

        boolean hasLiked = likeService.hasUserLikedPost(postId, user.getId());

        if (hasLiked) {
            likeService.unlikePost(postId, user.getId());
            return ResponseEntity.ok().body(Map.of(
                    "liked", false,
                    "count", likeService.countLikes(postId)
            ));
        } else {
            likeService.likePost(postId, user.getId());
            return ResponseEntity.ok().body(Map.of(
                    "liked", true,
                    "count", likeService.countLikes(postId)
            ));
        }
    }


    @GetMapping("/count")
    public ResponseEntity<Long> count(@PathVariable Long postId) {
        long total = likeService.countLikes(postId);
        return ResponseEntity.ok(total);
    }
}



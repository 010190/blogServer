package com.example.blogServer.controller;

import com.example.blogServer.entity.Post;
import com.example.blogServer.service.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Transient;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;


@Controller
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final StatisticsService statisticsService;

    @Autowired
    private CommentService commentService;


    @Autowired
    private LikeService likeService;

    @Autowired
    private UserService userService;

    @Autowired
    public PostController(PostService postService,
                          StatisticsService statisticsService,
                          LikeService likeService,
                          UserService userService) {
        this.postService = postService;
        this.statisticsService = statisticsService;
        this.likeService = likeService;
        this.userService = userService;

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
    public ResponseEntity<List<Post>> searchByTitle(@PathVariable String title) {
        return ResponseEntity.ok(postService.searchByTitle(title));
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

    @PostMapping("/{postId}/delete")
    @Transactional
    public String deletePost(Model model,
            @PathVariable Long postId,
            Principal principal,

            RedirectAttributes redirectAttributes)  {
        try {
            Long loggedUserId = userService.findUserIdByUsername(principal.getName());
            model.addAttribute("loggedUserId", loggedUserId);
            commentService.deleteByPostId(postId);
            likeService.removeLikesByPostId(postId);
            statisticsService.deleteByPostId(postId);
            boolean deleted = postService.deletePost(postId, loggedUserId);

            if (deleted) {
                redirectAttributes.addFlashAttribute("message", "Post został pomyślnie usunięty.");
            } else {
                redirectAttributes.addFlashAttribute("error", "Brak uprawnień do usunięcia tego posta.");
            }
        } catch (EntityNotFoundException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", "Wystąpił błąd podczas usuwania posta: " + ex.getMessage());
        }
        return "redirect:/";
    }

}



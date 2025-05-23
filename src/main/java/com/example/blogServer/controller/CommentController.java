package com.example.blogServer.controller;


import com.example.blogServer.entity.User;
import com.example.blogServer.service.CommentService;
import com.example.blogServer.service.StatisticsService;
import com.example.blogServer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping
@ControllerAdvice
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private StatisticsService statisticsService;

    @PostMapping("/comments/create")
    public String createComment(@RequestParam Long postId,
                                @RequestParam Long userId,
                                @RequestParam String content) {
        try {
            commentService.createComment(postId, userId, content);
            statisticsService.recordComment(postId);  // ← TU
            return "redirect:/post/" + postId;
        } catch (Exception e) {
            return "redirect:/post/" + postId + "?error=" + e.getMessage();
        }
    }




    @GetMapping("comments/{postId}")
    public ResponseEntity<?> getCommentByPostID(@PathVariable Long postId) {
        try {
            return ResponseEntity.ok(commentService.getCommentsByPostId(postId));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }

    @DeleteMapping("/comments/{commentId}")
    public String deleteComment(
            @RequestParam Long postId,
            @PathVariable Long commentId,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        String username = principal.getName();
        User user = userService.findByUsername(username);
        Long userId = user.getId();
        {
            try {
                boolean deleted = commentService.deleteComment(commentId, userId);
                if (deleted) {
                    statisticsService.removeComment(postId);
                    redirectAttributes.addFlashAttribute("successMessage", "Komentarz został usunięty");
                } else {
                    redirectAttributes.addFlashAttribute("errorMessage", "Brak uprawnień do usunięcia tego komentarza");
                }
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Wystąpił błąd podczas usuwania komentarza: " + e.getMessage());
            }

            return "redirect:/post/" + postId;
        }

    }
}

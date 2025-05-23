package com.example.blogServer.controller;

import com.example.blogServer.entity.User;
import com.example.blogServer.repository.UserRepository;
import com.example.blogServer.security.CustomUserDetails;
import com.example.blogServer.service.LikeService;
import com.example.blogServer.service.StatisticsService;
import com.example.blogServer.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/likes")
public class PostLikeController {

    private final LikeService likeService;
    private final UserService userService;
    private final UserRepository userRepository;

    private final StatisticsService statisticsService;

    @Autowired
    public PostLikeController(LikeService likeService, UserService userService, UserRepository userRepository, StatisticsService statisticsService) {
        this.likeService = likeService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.statisticsService = statisticsService;

    }

    @PostMapping
    public String toggleLike(@RequestParam Long postId,
                             @RequestParam String redirectUrl,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        if (authentication != null && authentication.isAuthenticated()) {
            Long userId = getUserId(authentication);

            boolean alreadyLiked = likeService.hasUserLikedPost(postId, userId);
            if (alreadyLiked) {
                likeService.unlikePost(postId, userId);
                statisticsService.removeLike(postId);
            } else {
                likeService.likePost(postId, userId);
                statisticsService.recordLike(postId);  // ← TU
            }
        }
        return "redirect:" + redirectUrl;
    }


    private Long getUserId(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            return ((CustomUserDetails) principal).getId();
        } else if (principal instanceof User) {
            return ((User) principal).getId();
        } else {
            String username = authentication.getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new EntityNotFoundException("Użytkownik nie znaleziony"));
            return user.getId();
        }
    }
}

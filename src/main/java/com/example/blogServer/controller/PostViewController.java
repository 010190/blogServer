package com.example.blogServer.controller;

import com.example.blogServer.entity.User;
import com.example.blogServer.service.PostService;
import com.example.blogServer.service.StatisticsService;
import com.example.blogServer.entity.Post;
import com.example.blogServer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostViewController {

    private final PostService postService;
    private final UserService userService; // Dodaj ten serwis

    @Autowired
    public PostViewController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping
    public String getAllPosts(Model model, Principal principal) {
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);

        if (principal != null) {
            String username = principal.getName();

            try {
                User currentUser = userService.findByUsername(username);
                model.addAttribute("currentUser", currentUser);

                System.out.println("Zalogowany użytkownik: " + username + " (ID: " + currentUser.getId() + ")");
            } catch (Exception e) {
                System.err.println("Nie można znaleźć użytkownika o nazwie: " + username);
                e.printStackTrace();
            }
        } else {
            System.out.println("Brak zalogowanego użytkownika (Principal jest null)");
        }

        return "post";
    }

}

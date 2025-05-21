package com.example.blogServer.controller;

import com.example.blogServer.entity.Post;
import com.example.blogServer.entity.User;
import com.example.blogServer.service.PostService;
import com.example.blogServer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class BlogController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home(Model model, Principal principal) {
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("allPosts", posts);

        boolean isAuthenticated = (principal != null);
        model.addAttribute("isAuthenticated", isAuthenticated);

        model.addAttribute("remoteUser", principal != null ? principal.getName() : null);

        if (principal != null) {
            String username = principal.getName();
            Optional<User> userOpt = userService.getUserByUsername(username);
            userOpt.ifPresent(user -> {
                model.addAttribute("userId", user.getId());
                model.addAttribute("currentUser", user);
            });
        }

        return "index";
    }
    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "About Us");
        return "about";
    }


    @GetMapping("/post")
    public String post(Model model, @RequestParam(required = false) Long id) {
        model.addAttribute("title", "Sample Post");

        if (id != null) {
            model.addAttribute("postId", id);
        }

        return "post";
    }


    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("title", "Contact Us");
        return "contact";
    }
}



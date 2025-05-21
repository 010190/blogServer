package com.example.blogServer.controller;

import com.example.blogServer.entity.Post;
import com.example.blogServer.entity.User;
import com.example.blogServer.service.PostService;
import com.example.blogServer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
            User user = userService.findByUsername(username);
            if (user != null) {
                model.addAttribute("userId", user.getId());
                model.addAttribute("currentUser", user);
            }
        }

        return "index";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "About Us");
        return "about";
    }

    @GetMapping("/post/{id}")
    public String post(Model model, @PathVariable Long id) {
        Post post = postService.getPostById(id);

        if (post != null) {
            model.addAttribute("post", post);
            model.addAttribute("title", post.getTitle());
            return "post";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("title", "Contact Us");
        return "contact";
    }
}



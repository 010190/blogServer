package com.example.blogServer.controller;

import com.example.blogServer.entity.Comment;
import com.example.blogServer.entity.Post;
import com.example.blogServer.service.CommentService;
import com.example.blogServer.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class BlogController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    @GetMapping("/")
    public String home(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("loggedIn", true);
            model.addAttribute("username", principal.getName());
        } else {
            model.addAttribute("loggedIn", false);
        }

        List<Post> posts = postService.getAllPosts();

        model.addAttribute("posts", posts);

        return "index";
    }

    @GetMapping("/about")
    public String about(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("loggedIn", true);
            model.addAttribute("username", principal.getName());
        } else {
            model.addAttribute("loggedIn", false);
        }
        return "about";
    }

    @GetMapping("/post/{id}")
    public String post(Model model, @PathVariable Long id, Principal principal) {
        if (principal != null) {
            model.addAttribute("loggedIn", true);
            model.addAttribute("username", principal.getName());
        } else {
            model.addAttribute("loggedIn", false);
        }

        Post post = postService.getPostById(id);
        model.addAttribute("post", post);

        List<Comment> comments = commentService.getCommentsByPostId(id);
        model.addAttribute("comments", comments);



        return "post";
    }

    @PostMapping("/post/{id}/comment")
    public String addComment(@PathVariable Long id,
                             @RequestParam String content,
                             Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            try {
                commentService.createComment(id, username, content);
            } catch (Exception e) {
            }
        }

        return "redirect:/post/" + id;
    }


    @GetMapping("/contact")
    public String contact(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("loggedIn", true);
            model.addAttribute("username", principal.getName());
        } else {
            model.addAttribute("loggedIn", false);
        }
        return "contact";
    }
}


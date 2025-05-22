package com.example.blogServer.controller;

import com.example.blogServer.entity.Post;
import com.example.blogServer.entity.User;
import com.example.blogServer.service.PostService;
import com.example.blogServer.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostCreateController {

    private final PostService postService;
    private final UserService userService;

    @GetMapping("/create")
    public String showCreatePostForm(Model model) {
        model.addAttribute("post", new Post());
        return "create";

    }

    @PostMapping("/create")
    public String createPost(@RequestParam String title,
                             @RequestParam String subtitle,
                             @RequestParam String content,
                             @RequestParam(required = false) String img) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User currentUser = userService.findByUsername(username);

        Post post = new Post();
        post.setTitle(title);
        post.setSubtitle(subtitle);
        post.setContent(content);
        post.setAuthor(username);
        post.setPostedBy(currentUser.getId());

        if (img == null || img.isEmpty()) {
            post.setImg("/assets/img/post-bg.jpg");
        } else {
            post.setImg(img);
        }

        Post savedPost = postService.savePost(post);

        return "redirect:/post/" + savedPost.getId();
    }
}

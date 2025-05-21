package com.example.blogServer.controller;

import com.example.blogServer.service.PostService;
import com.example.blogServer.service.StatisticsService;
import com.example.blogServer.entity.Post;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PostViewController {

    private final PostService postService;
    private final StatisticsService statisticsService;

    public PostViewController(PostService postService,
                              StatisticsService statisticsService) {
        this.postService = postService;
        this.statisticsService = statisticsService;
    }

    @GetMapping("/post")
    public String showAllPosts(Model model) {
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
        return "posts";
    }
}

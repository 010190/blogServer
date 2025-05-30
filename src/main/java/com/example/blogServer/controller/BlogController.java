package com.example.blogServer.controller;

import com.example.blogServer.entity.*;
import com.example.blogServer.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class BlogController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private TagService tagService;

    @Autowired
    private StatisticsService statisticsService;

    private void addUserToModel(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("loggedIn", true);
            model.addAttribute("username", principal.getName());

            User currentUser = userService.findByUsername(principal.getName());
            model.addAttribute("currentUser", currentUser);
        } else {
            model.addAttribute("loggedIn", false);
        }
    }

    @GetMapping("/")
    public String home(Model model, Principal principal) {
        addUserToModel(model, principal);

        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);

        List<Tag> tags = tagService.getAllTags();
        model.addAttribute("tags", tags);

        Long loggedUserId = null;
        if (principal != null) {
            loggedUserId = userService.findUserIdByUsername(principal.getName());
        }
        model.addAttribute("loggedUserId", loggedUserId);

        Map<Long, Map<String, Integer>> postStats = new HashMap<>();

        for (Post post : posts) {
            Map<String, Integer> stats = new HashMap<>();
            stats.put("views", post.getViewCount());

            long likeCount = likeService.countLikes(post.getId());
            stats.put("likes", (int) likeCount);


            int commentCount = commentService.countCommentsByPostId(post.getId());
            stats.put("comments", commentCount);

            postStats.put(post.getId(), stats);
        }


        model.addAttribute("postStats", postStats);

        return "index";
    }


    @GetMapping("/about")
    public String about(Model model, Principal principal) {
        addUserToModel(model, principal);
        return "about";
    }

    @GetMapping("/post/{id}")
    public String post(Model model, @PathVariable Long id, Principal principal) {
        Post post = postService.getPostById(id);
        model.addAttribute("post", post);

        if (principal != null) {
            User user = userService.findByUsername(principal.getName());
            boolean hasLiked = likeService.hasUserLikedPost(user.getId(), id);
            model.addAttribute("userHasLiked", hasLiked);
            model.addAttribute("currentUserId", user.getId());
            addUserToModel(model, principal);
            Long loggedUserId = null;
            if (principal != null) {
                loggedUserId = userService.findUserIdByUsername(principal.getName());
            }
            model.addAttribute("loggedUserId", loggedUserId);

        } else {
            model.addAttribute("userHasLiked", false);
            model.addAttribute("currentUserId", 0);
        }

        long likeCount = likeService.countLikes(id);
        model.addAttribute("likeCount", likeCount);

        List<Comment> comments = commentService.getCommentsByPostId(id);
        model.addAttribute("comments", comments);


        return "post";
    }



    @PostMapping("/post/{id}/comment")
    public String addComment(@PathVariable Long id,
                             @RequestParam String content,
                             Principal principal,
                             RedirectAttributes redirectAttributes) {
        if (principal != null) {
            String username = principal.getName();
            try {
                User user = userService.findByUsername(username);
                if (user != null) {
                    commentService.createComment(id, user.getId(), content);
                    redirectAttributes.addFlashAttribute("message", "Komentarz został dodany pomyślnie.");
                } else {
                    redirectAttributes.addFlashAttribute("error", "Nie znaleziono użytkownika.");
                }
            } catch (Exception e) {
                System.err.println("Błąd podczas dodawania komentarza: " + e.getMessage());
                redirectAttributes.addFlashAttribute("error", "Wystąpił błąd podczas dodawania komentarza: " + e.getMessage());
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Musisz być zalogowany, aby dodać komentarz.");
        }

        return "redirect:/post/" + id;
    }




    @GetMapping("/contact")
    public String contact(Model model, Principal principal) {
        addUserToModel(model, principal);
        return "contact";
    }

    @GetMapping("/search")
    public String searchPosts(@RequestParam("q") String query, Model model) {
        List<Post> posts = postService.searchByTitle(query);
        model.addAttribute("posts", posts);
        model.addAttribute("query", query);
        Map<Long, Map<String, Integer>> postStats = new HashMap<>();

        for (Post post : posts) {
            Map<String, Integer> stats = new HashMap<>();
            stats.put("views", post.getViewCount());

            long likeCount = likeService.countLikes(post.getId());
            stats.put("likes", (int) likeCount);


            int commentCount = commentService.countCommentsByPostId(post.getId());
            stats.put("comments", commentCount);

            postStats.put(post.getId(), stats);
        }
        model.addAttribute("postStats", postStats);

        return "search";
    }

    @GetMapping("/tag")
    public String searchPostsByTag(@RequestParam("title") String tagTitle, Model model) {
        List<Post> posts = postService.searchByTagTitle(tagTitle);
        model.addAttribute("posts", posts);
        model.addAttribute("query", "Posts tagged with: " + tagTitle);
        Map<Long, Map<String, Integer>> postStats = new HashMap<>();

        for (Post post : posts) {
            Map<String, Integer> stats = new HashMap<>();
            stats.put("views", post.getViewCount());

            long likeCount = likeService.countLikes(post.getId());
            stats.put("likes", (int) likeCount);


            int commentCount = commentService.countCommentsByPostId(post.getId());
            stats.put("comments", commentCount);

            postStats.put(post.getId(), stats);
        }
        model.addAttribute("postStats", postStats);

        return "search";
    }

    @GetMapping("/profile")
    public String userProfile(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/auth/login";
        }

        String username = principal.getName();
        User user = userService.findByUsername(username);
        Long userId = user.getId();

        List<Post> userPosts = postService.getUserPosts(userId);

        Map<Long, Map<String, Integer>> postStats = new HashMap<>();

        int totalViews = 0;
        int totalLikes = 0;
        int totalComments = 0;

        Long loggedUserId = null;
        if (principal != null) {
            loggedUserId = userService.findUserIdByUsername(principal.getName());
        }
        model.addAttribute("loggedUserId", loggedUserId);

        for (Post post : userPosts) {
            Map<String, Integer> stats = new HashMap<>();

            int views = post.getViewCount();
            int likes = (int) likeService.countLikes(post.getId());
            int comments = commentService.countCommentsByPostId(post.getId());

            stats.put("views", views);
            stats.put("likes", likes);
            stats.put("comments", comments);

            postStats.put(post.getId(), stats);

            totalViews += views;
            totalLikes += likes;
            totalComments += comments;
        }

        Map<String, Integer> totalStats = new HashMap<>();
        totalStats.put("views", totalViews);
        totalStats.put("likes", totalLikes);
        totalStats.put("comments", totalComments);

        model.addAttribute("username", username);
        model.addAttribute("userPosts", userPosts);
        model.addAttribute("postStats", postStats);
        model.addAttribute("totalStats", totalStats);

        return "user";
    }








}


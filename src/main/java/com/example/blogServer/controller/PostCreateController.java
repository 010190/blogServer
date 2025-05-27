package com.example.blogServer.controller;

import com.example.blogServer.entity.Post;
import com.example.blogServer.entity.Tag;
import com.example.blogServer.entity.User;
import com.example.blogServer.service.PostService;
import com.example.blogServer.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
                             @RequestParam(required = false) String img,
                             @RequestParam(required = false) String tags,
                             Principal principal,
                             RedirectAttributes redirectAttributes) {

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

        if (tags != null && !tags.trim().isEmpty()) {
            List<String> tagNames = Arrays.stream(tags.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());

            postService.addTagsToPost(savedPost.getId(), tagNames);
        }



        return "redirect:/post/" + savedPost.getId();
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Principal principal, Model model, RedirectAttributes redirectAttributes) {
        Post post = postService.getPostById(id);
        Long loggedUserId = userService.findUserIdByUsername(principal.getName());

        if (!post.getPostedBy().equals(loggedUserId)) {
            redirectAttributes.addFlashAttribute("error", "Nie masz uprawnień do edycji tego posta.");
            return "redirect:/";
        }

        String tagString = post.getTags().stream()
                .map(Tag::getTitle)
                .collect(Collectors.joining(","));

        model.addAttribute("post", post);
        model.addAttribute("tags", tagString);

        return "edit";
    }


    @PostMapping("/edit/{id}")
    public String updatePost(@PathVariable Long id,
                             @RequestParam String title,
                             @RequestParam String subtitle,
                             @RequestParam String content,
                             @RequestParam(required = false) String img,
                             @RequestParam(required = false) String tags,
                             Principal principal,
                             RedirectAttributes redirectAttributes) {

        Long loggedUserId = userService.findUserIdByUsername(principal.getName());
        Post existingPost = postService.getPostById(id);

        if (!existingPost.getPostedBy().equals(loggedUserId)) {
            redirectAttributes.addFlashAttribute("error", "Brak uprawnień do edycji posta.");
            return "redirect:/";
        }

        existingPost.setTitle(title);
        existingPost.setSubtitle(subtitle);
        existingPost.setContent(content);
        existingPost.setImg((img != null && !img.trim().isEmpty()) ? img : null);

        postService.savePost(existingPost);

        List<String> tagNames = (tags != null && !tags.trim().isEmpty())
                ? Arrays.stream(tags.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList())
                : Collections.emptyList();

        postService.updateTagsForPost(existingPost.getId(), tagNames);

        redirectAttributes.addFlashAttribute("message", "Post został zaktualizowany.");
        return "redirect:/post/" + id;
    }


}

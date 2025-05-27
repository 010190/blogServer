package com.example.blogServer.service;

import com.example.blogServer.entity.Post;
import com.example.blogServer.entity.Tag;
import com.example.blogServer.entity.User;
import com.example.blogServer.repository.PostRepository;
import com.example.blogServer.repository.TagRepository;
import com.example.blogServer.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikeService likeService;

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private TagRepository tagRepository;

    @Override
    public Post savePost(Post post) {

        return postRepository.save(post);
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Post getPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found! id=" + postId));

        post.incrementViewCount();
        statisticsService.recordView(postId);
        return postRepository.save(post);
    }

    @Override
    public void likePost(Long postId, Long userId) {

        likeService.likePost(postId, userId);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found! id=" + postId));
        post.incrementLikeCount();
        statisticsService.recordLike(postId);
        postRepository.save(post);
    }

    @Override
    public List<Post> searchByTitle(String title) {
        return postRepository.findAllByTitleContainingIgnoreCase(title);
    }

    @Override
    public List<Post> getRecentPosts(int limit) {
        return postRepository.findAll(
                PageRequest.of(0, limit, Sort.by("date").descending())
        ).getContent();
    }

    @Override
    public List<Post> getMostLikedPosts(int limit) {
        return postRepository.findAll(
                PageRequest.of(0, limit, Sort.by("likeCount").descending())
        ).getContent();
    }

    @Override
    public int getTotalUserLikes(Long userId) {

        return postRepository.findByPostedBy(userId)
                .stream()
                .mapToInt(Post::getLikeCount)
                .sum();
    }

    @Override
    public List<Post> getUserPosts(Long userId) {
        return postRepository.findByPostedBy(userId);
    }

    @Override
    public boolean deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post o ID " + postId + " nie został znaleziony"));

        if (userId == 1 || post.getPostedBy().equals(userId)) {

            postRepository.delete(post);
            return true;
        }

        return false;
    }


    @Override
    public void addTagsToPost(Long postId, List<String> tagNames) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Set<Tag> tags = tagNames.stream()
                .map(name -> tagRepository.findByTitle(name)
                        .orElseGet(() -> tagRepository.save(new Tag(name))))
                .collect(Collectors.toSet());

        post.setTags(tags);
        postRepository.save(post);
    }

    @Override
    public Post updatePost(Post post) {
        return postRepository.save(post);
    }

    @Override
    @Transactional
    public void updateTagsForPost(Long postId, List<String> tagNames) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        Set<Tag> tags = tagNames.stream()
                .map(name -> tagRepository.findByTitle(name)
                        .orElseGet(() -> tagRepository.save(new Tag(name))))
                .collect(Collectors.toSet());

        post.setTags(tags);
        postRepository.save(post);
    }


    @Override
    public List<Post> searchByTagTitle(String tagTitle) {
        return tagRepository.findByTitle(tagTitle)
                .map(tag -> {
                    Set<Post> postSet = tag.getPosts();
                    return new ArrayList<Post>(postSet);
                })
                .orElse(new ArrayList<>());
    }





}


package com.example.blogServer.service;

import com.example.blogServer.entity.Like;
import com.example.blogServer.entity.Post;
import com.example.blogServer.entity.User;
import com.example.blogServer.repository.LikeRepository;
import com.example.blogServer.repository.PostRepository;
import com.example.blogServer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl implements LikeService {
    @Autowired
    private LikeRepository likeRepo;

    @Autowired
    private PostRepository postRepo;

    @Autowired
    private UserRepository userRepo;

    @Override
    public void likePost(Long postId, Long userId) {
        Post post = postRepo.findById(postId).orElseThrow();
        User user = userRepo.findById(userId).orElseThrow();
        Like like = new Like(user, post);
        likeRepo.save(like);
    }

    @Override
    public long countLikes(Long postId) {
        return likeRepo.countByPostId(postId);
    }
}


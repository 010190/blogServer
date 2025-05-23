package com.example.blogServer.service;

import com.example.blogServer.entity.Like;
import com.example.blogServer.entity.Post;
import com.example.blogServer.entity.User;
import com.example.blogServer.repository.LikeRepository;
import com.example.blogServer.repository.PostRepository;
import com.example.blogServer.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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

    @Autowired
    private PostRepository postRepository;

    @Override
    public void likePost(Long postId, Long userId) {



        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post o ID " + postId + " nie istnieje"));

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Użytkownik o ID " + userId + " nie istnieje"));


        if (likeRepo.existsByPostIdAndUserId(postId, userId)) {
            return;
        }

        Like like = new Like(user, post);
        likeRepo.save(like);
    }

    @Override
    public long countLikes(Long postId) {
        return likeRepo.countByPostId(postId);
    }


    @Override
    public boolean hasUserLikedPost(Long postId, Long userId) {
        return likeRepo.existsByPostIdAndUserId(postId, userId);
    }

    @Override
    @Transactional
    public void unlikePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post o ID " + postId + " nie został znaleziony"));

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Użytkownik o ID " + userId + " nie został znaleziony"));

        Like like = likeRepo.findByPostAndUser(post, user)
                .orElse(null);

        if (like != null) {
            likeRepo.delete(like);
        }
    }
    @Override
    public void removeLikesByPostId(Long postId) {
        likeRepo.deleteByPostId(postId);
    }


}


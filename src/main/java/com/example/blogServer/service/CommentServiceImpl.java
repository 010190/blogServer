package com.example.blogServer.service;

import com.example.blogServer.entity.Comment;
import com.example.blogServer.entity.Post;
import com.example.blogServer.entity.User;
import com.example.blogServer.repository.CommentRepository;
import com.example.blogServer.repository.PostRepository;
import com.example.blogServer.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Comment createComment(Long postId, Long userId, String content) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setCreatedAt(new Date());
        comment.setPostedBy(user.getName());
        comment.setUserId(userId);
        comment.setPost(post);

        return commentRepository.save(comment);
    }
    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    @Override
    @Transactional
    public void deleteByPostId(Long postId) {
        commentRepository.deleteByPostId(postId);
    }

    @Override
    @Transactional
    public boolean deleteComment(Long commentId, Long userId) {
        Optional<Comment> commentOpt = commentRepository.findById(commentId);

        if (commentOpt.isEmpty()) {
            return false;
        }

        Comment comment = commentOpt.get();

        if (comment.getPost() == null) {
            return false;
        }


        if (userId == 1 ||
                comment.getUserId().equals(userId) ||
                comment.getPost().getPostedBy().equals(userId)) {

            commentRepository.deleteById(commentId);
            return true;
        }

        return false;
    }
    @Override
    public int countCommentsByPostId(Long postId) {
        return commentRepository.countByPostId(postId);
    }
}

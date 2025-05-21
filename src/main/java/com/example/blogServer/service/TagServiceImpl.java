package com.example.blogServer.service;

import com.example.blogServer.entity.Post;
import com.example.blogServer.entity.Tag;
import com.example.blogServer.repository.PostRepository;
import com.example.blogServer.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    @Autowired private TagRepository tagRepo;
    @Autowired private PostRepository postRepo;

    @Override
    public Tag createTag(String title) {
        return tagRepo.findByTitle(title)
                .orElseGet(() -> tagRepo.save(new Tag(title)));
    }


    @Override
    public List<Tag> getAllTags() {
        return tagRepo.findAll();
    }

    @Override
    public List<Post> getPostsByTag(Long tagId) {
        Tag tag = tagRepo.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Tag o ID " + tagId + " nie istnieje"));
        return List.copyOf(tag.getPosts());
    }

}

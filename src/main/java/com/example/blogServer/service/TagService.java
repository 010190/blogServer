package com.example.blogServer.service;

import com.example.blogServer.entity.Post;
import com.example.blogServer.entity.Tag;
import java.util.List;

public interface TagService {
    Tag createTag(String title);
    List<Tag> getAllTags();
    List<Post> getPostsByTag(Long tagId);
    void deleteTag(Long id);
}

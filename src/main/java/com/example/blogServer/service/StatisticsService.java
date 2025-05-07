package com.example.blogServer.service;

import com.example.blogServer.entity.Statistics;
import org.springframework.stereotype.Service;

import java.util.List;


public interface StatisticsService {
    void recordView(Long postId);
    void recordLike(Long postId);
    void recordComment(Long postId);
    List<Statistics> getPostPerformance(Long postId);
}


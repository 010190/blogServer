package com.example.blogServer.service;

import com.example.blogServer.entity.Statistics;
import com.example.blogServer.repository.StatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StatisticsServiceImpl implements StatisticsService {
    @Autowired private StatisticsRepository statsRepo;

    @Override
    public void recordView(Long postId) {
        statsRepo.save(new Statistics(postId, 1, 0, 0));
    }
    @Override
    public void recordLike(Long postId) {
        statsRepo.save(new Statistics(postId, 0, 1, 0));
    }
    @Override
    public void recordComment(Long postId) {
        statsRepo.save(new Statistics(postId, 0, 0, 1));
    }
    @Override
    public List<Statistics> getPostPerformance(Long postId) {
        return statsRepo.findByPostId(postId);
    }
}

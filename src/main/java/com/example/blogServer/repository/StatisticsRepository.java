package com.example.blogServer.repository;

import com.example.blogServer.entity.Statistics;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StatisticsRepository extends JpaRepository<Statistics, Long> {
    List<Statistics> findByPostId(Long postId);


    @Transactional
    void deleteByPostId(Long postId);
}

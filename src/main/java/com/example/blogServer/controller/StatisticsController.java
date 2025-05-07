package com.example.blogServer.controller;

import com.example.blogServer.entity.Statistics;
import com.example.blogServer.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {
    @Autowired private StatisticsService statsService;

    @GetMapping("/post/{postId}")
    public List<Statistics> postStats(@PathVariable Long postId) {
        return statsService.getPostPerformance(postId);
    }
}

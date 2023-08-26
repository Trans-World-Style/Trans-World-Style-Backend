package com.example.transback.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.example.transback.service.VideoService;

import java.time.LocalDateTime;

@Service
public class VideoScheduler {

    private final VideoService videoService;

    @Autowired
    public VideoScheduler(VideoService videoService) {
        this.videoService = videoService;
    }

    @Scheduled(cron = "0 0 9 * * ?") // 실행 시간
    public void scheduleVideoDeletion() {
        System.out.println("Scheduled task executed at " + LocalDateTime.now());
        videoService.updateDeleteStateForOldVideos();
    }
}

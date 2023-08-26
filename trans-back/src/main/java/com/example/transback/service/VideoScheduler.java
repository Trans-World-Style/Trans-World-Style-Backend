package com.example.transback.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.example.transback.service.VideoService;

@Service
public class VideoScheduler {

    private final VideoService videoService;

    @Autowired
    public VideoScheduler(VideoService videoService) {
        this.videoService = videoService;
    }

    @Scheduled(cron = "0 30 13 * * ?") // 실행 시간
    public void scheduleVideoDeletion() {
        videoService.updateDeleteStateForOldVideos();
    }
}

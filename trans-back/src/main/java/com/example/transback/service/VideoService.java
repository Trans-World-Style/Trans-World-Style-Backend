package com.example.transback.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.example.transback.config.datasource.DataSourceType;
import com.example.transback.config.replication.ReplicationRoutingDataSource;
import com.example.transback.dao.VideoDAOInterface;
import com.example.transback.dto.VideoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VideoService {

    @Autowired
    private VideoDAOInterface videoRepository;

    @Transactional(readOnly = true)
    public List<VideoDTO> findAll() {
        List<VideoDTO> list = videoRepository.findAll();
        System.out.println("service result>> " + list);
        return list;
    }

    @Transactional(readOnly = true)
    public Optional<VideoDTO> findById(Integer video_id){
        Optional<VideoDTO> vo = videoRepository.findById(video_id);
        System.out.println("service result>> " + vo);
        return vo;
    }

    @Transactional(readOnly = false)
    public VideoDTO save(VideoDTO vo) {
        VideoDTO vo2 = videoRepository.save(vo);
        return vo2;
    }

    @Transactional(readOnly = true)
    public List<VideoDTO> findVideosByEmailAndDeleteZero(String email) {
        List<VideoDTO> list = videoRepository.findVideosByEmailAndDeleteZero(email);
        System.out.println("service result>> " + list);
        return list;
    }

    @Transactional(readOnly = false)
    public VideoDTO updateDeleteState(int video_id) {
        Optional<VideoDTO> optionalVideo = videoRepository.findById(video_id);
        System.out.println("service result>> " + optionalVideo);
        if (optionalVideo.isPresent()) {
            VideoDTO video = optionalVideo.get();
            video.setDelete_state(1);
            VideoDTO updatedVideo = videoRepository.save(video);
            return updatedVideo;
        } else {
            throw new NotFoundException("Video not found with ID: " + video_id);
        }
    }

    @Transactional(readOnly = true)
    public void updateDeleteStateForOldVideos() {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        List<VideoDTO> oldVideos = videoRepository.findVideosByUploadTimeBeforeAndDeleteStateIsZero(sevenDaysAgo);

        for (VideoDTO video : oldVideos) {
            video.setDelete_state(1);
            videoRepository.save(video);
        }
    }

}





package com.example.transback.service;

import com.example.transback.VideoDAOInterface;
import com.example.transback.VideoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VideoService {

    @Autowired
    private VideoDAOInterface videoRepository;

    public List<VideoVO> findAll() {
        List<VideoVO> list = videoRepository.findAll();
        System.out.println("service result>> " + list);
        return list;
    }

    public Optional<VideoVO> findById(Integer video_id){
        Optional<VideoVO> vo = videoRepository.findById(video_id);
        System.out.println("service result>> " + vo);
        return vo;
    }

    public VideoVO save(VideoVO vo) {
        VideoVO vo2 = videoRepository.save(vo);
        return vo2;
    }

}

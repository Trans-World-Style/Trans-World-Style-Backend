package com.example.transback.service;

import com.example.transback.dao.VideoDAOInterface;
import com.example.transback.dto.VideoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(VideoService.class)
class VideoServiceTest {

    @MockBean
    private VideoDAOInterface videoDAOInterface;

    @InjectMocks
    private VideoService videoService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll() {

        // 가짜 데이터 생성
        VideoDTO video1 = new VideoDTO();
        video1.setVideo_id(1);
        video1.setVideo_name("Video1");

        VideoDTO video2 = new VideoDTO();
        video2.setVideo_id(2);
        video2.setVideo_name("Video2");

        List<VideoDTO> fakeVideoList = new ArrayList<>();
        fakeVideoList.add(video1);
        fakeVideoList.add(video2);

        // VideoDAOInterface의 findAll() 메소드를 모킹하여 가짜 데이터 반환하도록 설정
        given(videoDAOInterface.findAll()).willReturn(fakeVideoList);

        // 테스트 실행
        List<VideoDTO> result = videoService.findAll();

        // 결과 검증
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Video1", result.get(0).getVideo_name());
        assertEquals("Video2", result.get(1).getVideo_name());

    }

    @Test
    void findById() {
        int videoId = 1;

        // 가짜 데이터 생성
        VideoDTO video = new VideoDTO();
        video.setVideo_id(videoId);
        video.setVideo_name("Video1");

        // VideoDAOInterface의 findById() 메소드를 모킹하여 가짜 데이터 반환하도록 설정
        given(videoDAOInterface.findById(videoId)).willReturn(Optional.of(video));

        // 테스트 실행
        Optional<VideoDTO> result = videoService.findById(videoId);

        // 결과 검증
        assertTrue(result.isPresent());
        assertEquals("Video1", result.get().getVideo_name());

    }

    @Test
    void save() {
        // 가짜 데이터 생성
        VideoDTO video = new VideoDTO();
        video.setVideo_id(1);
        video.setVideo_name("Video1");

        // VideoDAOInterface의 save() 메소드를 모킹하여 가짜 데이터 반환하도록 설정
        given(videoDAOInterface.save(any(VideoDTO.class))).willReturn(video);

        // 테스트 실행
        VideoDTO result = videoService.save(video);

        // 결과 검증
        assertNotNull(result);
        assertEquals("Video1", result.getVideo_name());

        // VideoDAOInterface의 save() 메소드가 호출되었는지 검증
        verify(videoDAOInterface).save(any(VideoDTO.class));
    }

    @Test
    void findVideosByEmailAndDeleteZero() {
        String email = "test@example.com";

        // 가짜 데이터 생성
        VideoDTO video1 = new VideoDTO();
        video1.setVideo_id(1);
        video1.setEmail(email);
        video1.setDelete_state(0);
        video1.setVideo_name("Video1");

        VideoDTO video2 = new VideoDTO();
        video2.setVideo_id(2);
        video2.setEmail(email);
        video2.setDelete_state(1);
        video2.setVideo_name("Video2");

        List<VideoDTO> fakeVideoList = new ArrayList<>();
        fakeVideoList.add(video1);

        // VideoDAOInterface의 findVideosByEmailAndDeleteZero() 메소드를 모킹하여 가짜 데이터 반환하도록 설정
        given(videoDAOInterface.findVideosByEmailAndDeleteZero(email)).willReturn(fakeVideoList);

        // 테스트 실행
        List<VideoDTO> result = videoService.findVideosByEmailAndDeleteZero(email);
        System.out.println(result);
        // 결과 검증
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Video1", result.get(0).getVideo_name());
    }

    @Test
    void updateDeleteState() {
        int videoId = 1;

        // 가짜 데이터 생성
        VideoDTO video = new VideoDTO();
        video.setVideo_id(videoId);
        video.setDelete_state(0);

        // VideoDAOInterface의 findById() 메소드를 모킹하여 가짜 데이터 반환하도록 설정
        when(videoDAOInterface.findById(videoId)).thenReturn(Optional.of(video));

        // VideoDAOInterface의 save() 메소드를 모킹하여 가짜 데이터 반환하도록 설정
        when(videoDAOInterface.save(any(VideoDTO.class))).thenReturn(video);

        // 테스트 실행
        VideoDTO result = videoService.updateDeleteState(videoId);

        // 결과 검증
        assertNotNull(result);
        assertEquals(1, result.getDelete_state());

        // VideoDAOInterface의 findById()와 save() 메소드가 호출되었는지 검증
        verify(videoDAOInterface).findById(videoId);
        verify(videoDAOInterface).save(any(VideoDTO.class));
    }
}
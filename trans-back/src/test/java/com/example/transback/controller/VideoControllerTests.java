package com.example.transback.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.example.transback.dto.VideoDTO;
import com.example.transback.service.FileUploadService;
import com.example.transback.service.VideoService;
import com.example.transback.util.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
//import static org.mockito.Mockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@WebMvcTest(VideoController.class)
@ActiveProfiles("test") // 테스트 환경용 프로파일로 설정
public class VideoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate; // Mock으로 주입

    @MockBean
    private VideoService videoService;

    @MockBean
    private FileUploadService fileUploadService;

    @MockBean
    private AmazonS3 amazonS3; // AmazonS3를 Mock으로 주입

    private static class AIResponse {
        private String status;
        private String result;

        public void setResult(String result) {
            this.result = result;
        }
    }



    @Test
    @DisplayName("모든 video list 가져오기 테스트")
    public void testFindAll() throws Exception {

        // 가짜 데이터 생성
        VideoDTO video1 = new VideoDTO();
        video1.setVideo_id(85);
        video1.setEmail("pch14545@gmail.com");
        video1.setVideo_link("fb330f64-abb2-43cc-bee7-785711599e9f3번인사 제리 720p.mp4");
        video1.setUpload_url(null);
        video1.setOutput_url(null);
        video1.setVideo_name("3번인사 제리 720p.mp4");
        video1.setUpload_time(LocalDateTime.parse("2023-07-17T03:50:19"));
        video1.setDelete_state(1);
        video1.setDelete_time(null);

        VideoDTO video2 = new VideoDTO();
        video2.setVideo_id(89);
        video2.setEmail("zzxx9633@gmail.com");
        video2.setVideo_link("791a67e7-8274-4f2b-810b-b5e5d374582f166808 (360p).mp4");
        video2.setUpload_url("https://trans-world-style.s3.ap-northeast-2.amazonaws.com/upload/791a67e7-8274-4f2b-810b-b5e5d374582f166808%20%28360p%29.mp4?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20230717T055557Z&X-Amz-SignedHeaders=host&X-Amz-Expires=900&X-Amz-Credential=AKIA4NJHVZKRCWWUG5KJ%2F20230717%2Fap-northeast-2%2Fs3%2Faws4_request&X-Amz-Signature=c200e8a6d46de8573b2ae411ba8f5fa3742310347013f07b96187d692646095c");
        video2.setOutput_url("https://trans-world-style.s3.ap-northeast-2.amazonaws.com/output/791a67e7-8274-4f2b-810b-b5e5d374582f166808%20%28360p%29.mp4?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20230717T055745Z&X-Amz-SignedHeaders=host&X-Amz-Expires=3599&X-Amz-Credential=AKIA4NJHVZKRCWWUG5KJ%2F20230717%2Fap-northeast-2%2Fs3%2Faws4_request&X-Amz-Signature=8972b9acf5e3d76c8f2fb093624f5c20669abffa26ed8844169a4c0cc7be75a9");
        video2.setVideo_name("166808 (360p).mp4");
        video2.setUpload_time(LocalDateTime.parse("2023-07-17T05:55:57"));
        video2.setDelete_state(0);
        video2.setDelete_time(null);

        List<VideoDTO> fakeVideoList = Arrays.asList(video1, video2);

        // VideoService의 findAll() 메서드가 호출될 때 가짜 데이터를 반환하도록 설정
        given(videoService.findAll()).willReturn(fakeVideoList);

        mockMvc.perform(MockMvcRequestBuilders.get("/video/list"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].video_id").value(85))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("pch14545@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].video_name").value("3번인사 제리 720p.mp4"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].video_id").value(89))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].email").value("zzxx9633@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].video_name").value("166808 (360p).mp4"))
                .andDo(print());
        verify(videoService).findAll();

    }

    @Test
    @DisplayName("특정 이메일로 삭제되지 않은 video 목록 가져오기 테스트")
    public void testFindVideosByEmailAndDeleteZero() throws Exception {
        // 가짜 JWT 토큰 생성
        //String jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiZW1haWwiOiJ6enh4OTYzM0BnbWFpbC5jb20iLCJpYXQiOjE2ODk3NTk2ODAsImV4cCI6MTc3NjE1OTY4MH0.n2ZfINwuxRBWsUyzlWD9O-aGC6R1pkp0esFRi67_bSc";
        // 가짜 이메일
        String email = "zzxx9633@gmail.com";
        String jwtToken = JwtUtil.generateJWT(email);

        // 가짜 데이터 생성
        VideoDTO video1 = new VideoDTO();
        video1.setVideo_id(85);
        video1.setEmail(email);
        video1.setVideo_link("fb330f64-abb2-43cc-bee7-785711599e9f3번인사 제리 720p.mp4");
        video1.setUpload_url(null);
        video1.setOutput_url(null);
        video1.setVideo_name("3번인사 제리 720p.mp4");
        video1.setUpload_time(LocalDateTime.parse("2023-07-17T03:50:19"));
        video1.setDelete_state(0);
        video1.setDelete_time(null);

        VideoDTO video2 = new VideoDTO();
        video2.setVideo_id(89);
        video2.setEmail(email);
        video2.setVideo_link("791a67e7-8274-4f2b-810b-b5e5d374582f166808 (360p).mp4");
        video2.setUpload_url("https://trans-world-style.s3.ap-northeast-2.amazonaws.com/upload/791a67e7-8274-4f2b-810b-b5e5d374582f166808%20%28360p%29.mp4?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20230717T055557Z&X-Amz-SignedHeaders=host&X-Amz-Expires=900&X-Amz-Credential=AKIA4NJHVZKRCWWUG5KJ%2F20230717%2Fap-northeast-2%2Fs3%2Faws4_request&X-Amz-Signature=c200e8a6d46de8573b2ae411ba8f5fa3742310347013f07b96187d692646095c");
        video2.setOutput_url("https://trans-world-style.s3.ap-northeast-2.amazonaws.com/output/791a67e7-8274-4f2b-810b-b5e5d374582f166808%20%28360p%29.mp4?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20230717T055745Z&X-Amz-SignedHeaders=host&X-Amz-Expires=3599&X-Amz-Credential=AKIA4NJHVZKRCWWUG5KJ%2F20230717%2Fap-northeast-2%2Fs3%2Faws4_request&X-Amz-Signature=8972b9acf5e3d76c8f2fb093624f5c20669abffa26ed8844169a4c0cc7be75a9");
        video2.setVideo_name("166808 (360p).mp4");
        video2.setUpload_time(LocalDateTime.parse("2023-07-17T05:55:57"));
        video2.setDelete_state(0);
        video2.setDelete_time(null);

        List<VideoDTO> fakeVideoList = Arrays.asList(video1, video2);

        // VideoService의 findVideosByEmailAndDeleteZero() 메서드가 호출될 때 가짜 데이터를 반환하도록 설정
        given(videoService.findVideosByEmailAndDeleteZero(email)).willReturn(fakeVideoList);

        mockMvc.perform(MockMvcRequestBuilders.post("/video/list/email")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].video_id").value(85))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value(email))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].video_name").value("3번인사 제리 720p.mp4"));
    }

}

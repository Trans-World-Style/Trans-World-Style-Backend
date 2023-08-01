package com.example.transback.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.example.transback.dto.VideoDTO;
import com.example.transback.service.FileUploadService;
import com.example.transback.service.VideoService;
import com.example.transback.util.JwtUtil;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class VideoControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

//    @Test
//    @DisplayName("Video list (all)")
//    public void testFindAll() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/video/list"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].video_id").value(0))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("zzxx9633@gmail.com"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].video_name").value("sea.mp4"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[1].video_id").value(1))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[1].email").value("zzxx9633@gmail.com"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[1].video_name").value("sky.mp4"))
//                .andDo(print());
//    }

    @Test
    @DisplayName("Video list (private)")
    public void testFindVideosByEmailAndDeleteZero() throws Exception {
        // 테스트 이메일
        String email = "zzxx9633@gmail.com";
        String jwtToken = JwtUtil.generateJWT(email);

        mockMvc.perform(MockMvcRequestBuilders.post("/video/list/email")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].email", Matchers.everyItem(Matchers.is(email))))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].delete_state", Matchers.everyItem(Matchers.is(0))));
    }

    @Test
    @DisplayName("Update video delete state")
    public void testUpdateDeleteState() throws Exception {
        // 테스트용 video_id
        int videoId = 1;

        mockMvc.perform(MockMvcRequestBuilders.put("/video/update/delete_state/{video_id}", videoId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.video_id").value(videoId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.delete_state").value(1));
    }

//    @Test
//    @DisplayName("Upload video")
//    public void testUploadVideo() throws Exception {
//        // 테스트 이메일
//        String email = "zzxx9633@gmail.com";
//        String jwtToken = JwtUtil.generateJWT(email);
//
//        // 테스트용 파일 경로
//        String testFilePath = "src/test/resources/static/upload/cut(360p).mp4";
//        Path path = Paths.get(testFilePath);
//        String originalFilename = path.getFileName().toString();
//        MockMultipartFile file = new MockMultipartFile("file", originalFilename, MediaType.MULTIPART_FORM_DATA_VALUE, Files.readAllBytes(path));
//
//        mockMvc.perform(MockMvcRequestBuilders.multipart("/video/upload")
//                        .file(file)
//                        .header("Authorization", "Bearer " + jwtToken))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.upload_url").isNotEmpty())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.output_url").isNotEmpty());
//    }



}

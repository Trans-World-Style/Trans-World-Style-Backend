package com.example.transback.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class FileUploadServiceTest {
    @Mock
    private AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @InjectMocks
    private FileUploadService fileUploadService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void uploadFile() throws IOException{
        // 가짜 파일 생성
        String folderName = "test-folder";
        String savedName = "test-file.mp4";
        String filePath = "src/main/resources/static/upload/cut(360p).mp4";
        File file = new File(filePath);
        MultipartFile multipartFile = new MockMultipartFile(savedName, new FileInputStream(file));

        // AmazonS3의 putObject 메서드가 호출될 때 가짜 응답을 반환하도록 설정
        when(amazonS3.putObject(any(PutObjectRequest.class))).thenReturn(null);

        // 파일 업로드 메서드 실행
        String result = fileUploadService.uploadFile(multipartFile, savedName, folderName);
        System.out.println(result);
        // 결과 확인
        assertEquals(savedName, result);
    }

}
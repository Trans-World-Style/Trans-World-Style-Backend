package com.example.transback.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileUploadService {
    private final AmazonS3 amazonS3;
    private final String bucketName;

    @Autowired
    public FileUploadService(AmazonS3 amazonS3, @Value("${cloud.aws.s3.bucket}") String bucketName) {
        this.amazonS3 = amazonS3;
        this.bucketName = bucketName;
    }

    public String uploadFile(MultipartFile file, String savedName) throws IOException {
        String key =  "upload/" + savedName;

        // S3에 파일 업로드
        amazonS3.putObject(new PutObjectRequest(bucketName, key, file.getInputStream(), null).withCannedAcl(CannedAccessControlList.PublicRead));

        return savedName;
    }
}

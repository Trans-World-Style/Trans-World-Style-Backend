package com.example.transback.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import java.net.URL;
import java.util.Date;

@Service
public class FileUploadService {
    private final AmazonS3 amazonS3;
    private final String bucketName;

    @Autowired
    public FileUploadService(AmazonS3 amazonS3, @Value("${cloud.aws.s3.bucket}") String bucketName) {
        this.amazonS3 = amazonS3;
        this.bucketName = bucketName;
    }

    public String uploadFile(MultipartFile file, String savedName, String folderName) throws IOException {
        String key = folderName + "/" + savedName;
        // S3에 파일 업로드
        amazonS3.putObject(new PutObjectRequest(bucketName, key, file.getInputStream(), null).withCannedAcl(CannedAccessControlList.Private));
        return savedName;
    }

    public String generateSignedURL(String savedName,String folderName, long expirationTimeInMilliseconds) {
        String key = folderName + "/" + savedName;
        Date expiration = new Date(System.currentTimeMillis() + expirationTimeInMilliseconds);
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, key)
                .withExpiration(expiration);
        URL signedURL = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
        return signedURL.toString();
    }

    public String generateSignedURL2(String key, long expirationTimeInMilliseconds) {
        Date expiration = new Date(System.currentTimeMillis() + expirationTimeInMilliseconds);
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, key)
                .withExpiration(expiration);
        URL signedURL = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
        return signedURL.toString();
    }

}

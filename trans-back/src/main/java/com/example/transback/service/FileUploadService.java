package com.example.transback.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;

import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.apache.commons.io.FileUtils;



@Service
public class FileUploadService {
    private final AmazonS3 amazonS3;
    private final String bucketName;

    @Value("${ffmpeg.path}")
    private String ffmpegPath;

    @Autowired
    public FileUploadService(AmazonS3 amazonS3, @Value("${cloud.aws.s3.bucket}") String bucketName) {
        this.amazonS3 = amazonS3;
        this.bucketName = bucketName;
    }

    public String uploadFile(MultipartFile file, String savedName, String folderName) throws IOException {
        String key = folderName + "/" + savedName;

        // S3에 파일 업로드
        amazonS3.putObject(new PutObjectRequest(bucketName, key, file.getInputStream(), null).withCannedAcl(CannedAccessControlList.PublicRead));

        return savedName;
    }

    public String uploadFile2(File file, String savedName, String folderName) throws IOException {
        String key = folderName + "/" + savedName;

        // S3에 파일 업로드
        amazonS3.putObject(new PutObjectRequest(bucketName, key, file).withCannedAcl(CannedAccessControlList.PublicRead));

        return savedName;
    }

    // 영상에서 소리를 제거하는 메서드
    public File removeAudioFromVideo(MultipartFile videoFile) throws IOException, InterruptedException {
        // 임시 파일 디렉토리 생성
        File tempDir = Files.createTempDirectory("temp").toFile();

        // 영상 파일을 임시 디렉토리에 저장
        File tempVideoFile = new File(tempDir, "video.mp4");

        // MultipartFile을 임시 파일로 저장
        //videoFile.transferTo(tempVideoFile);

        // MultipartFile을 복사하여 임시 파일로 저장
        try (OutputStream os = new FileOutputStream(tempVideoFile)) {
            StreamUtils.copy(videoFile.getInputStream(), os);
        }

        System.out.println(tempVideoFile.exists());
        if (!tempVideoFile.exists()) {
            throw new FileNotFoundException("임시 파일이 생성되지 않았습니다.");
        }

        // FFmpeg 실행 파일 경로
        String ffmpegExecutable = new ClassPathResource(ffmpegPath).getFile().getAbsolutePath();

        System.out.println(1);

        // 변환된 파일 경로
        String outputFilePath = tempVideoFile.getAbsolutePath().replace(".mp4", "_muted.mp4");

        System.out.println(2);

        // FFmpeg을 사용하여 소리 제거
        String[] cmd = {ffmpegExecutable, "-i", tempVideoFile.getAbsolutePath(), "-an", "-vcodec", "copy", outputFilePath};
        ProcessBuilder pb = new ProcessBuilder(cmd);
        Process process = pb.start();
        process.waitFor();

        System.out.println(3);

        // 임시 파일 삭제
        tempVideoFile.delete();

        System.out.println(4);

        // 변환된 파일을 MultipartFile로 생성
        return new File(outputFilePath);
    }

    public File extractAudioFromVideo(MultipartFile videoFile) throws IOException, InterruptedException {
        //File tempVideoFile = File.createTempFile("temp-", ".mp4"); // 임시 파일 생성

        // 임시 파일 디렉토리 생성
        File tempDir = Files.createTempDirectory("temp").toFile();

        // 영상 파일을 임시 디렉토리에 저장
        File tempVideoFile = new File(tempDir, "video.mp4");

        // MultipartFile을 임시 파일로 저장
        //videoFile.transferTo(tempVideoFile);

        // MultipartFile을 복사하여 임시 파일로 저장
        try (OutputStream os = new FileOutputStream(tempVideoFile)) {
            StreamUtils.copy(videoFile.getInputStream(), os);
        }

        System.out.println(tempVideoFile.exists());
        if (!tempVideoFile.exists()) {
            throw new FileNotFoundException("임시 파일이 생성되지 않았습니다.");
        }

        // FFmpeg 실행 파일 경로
        String ffmpegExecutable = new ClassPathResource(ffmpegPath).getFile().getAbsolutePath();

        System.out.println(1);

        // 변환된 파일 경로
        String outputFilePath = tempVideoFile.getAbsolutePath().replace(".mp4", "_sound.mp4");

        System.out.println(2);

        // FFmpeg을 사용하여 소리 추출
        String[] cmd = {ffmpegExecutable, "-i", tempVideoFile.getAbsolutePath(), "-vn", "-acodec", "copy", outputFilePath};
        ProcessBuilder pb = new ProcessBuilder(cmd);
        Process process = pb.start();
        process.waitFor();

        System.out.println(3);

        // 임시 파일 삭제
        tempVideoFile.delete();

        System.out.println(4);

        // 변환된 파일을 File 객체로 생성
        return new File(outputFilePath);
    }


}

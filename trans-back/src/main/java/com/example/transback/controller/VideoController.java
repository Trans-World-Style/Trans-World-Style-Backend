package com.example.transback.controller;

import com.example.transback.dto.VideoDTO;
import com.example.transback.service.FileUploadService;
import com.example.transback.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import static com.example.transback.util.JwtUtil.extractEmailFromJWT;
import static com.example.transback.util.JwtUtil.validateJWT;


@RestController
@RequestMapping("/video")
@CrossOrigin(origins = "http://localhost:3000","http://endnjs.iptime.org:12510")
public class VideoController {

    @Autowired
    VideoService videoService;

    private final FileUploadService fileUploadService;

    @Autowired
    public VideoController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @GetMapping("/list")
    public List<VideoDTO> findAll() {
        List<VideoDTO> list = videoService.findAll();
        System.out.println("controller result>> " + list);
        //model.addAttribute("list", list);
        return list;
    }

    @PostMapping("/list/email")
    public ResponseEntity<List<VideoDTO>> findVideosByEmailAndDeleteZero(HttpServletRequest request) {
        // JWT 토큰에서 이메일 추출
        String jwt = request.getHeader("Authorization");
        jwt = jwt.replace("Bearer ", ""); // "Bearer " 접두사 제거
        System.out.println(jwt);

        if(validateJWT(jwt)) {
            String email = extractEmailFromJWT(jwt); // JWT 토큰에서 이메일 추출하는 함수 호출
            System.out.println(email);
            List<VideoDTO> list = videoService.findVideosByEmailAndDeleteZero(email);
            System.out.println("controller result>> " + list);
            return ResponseEntity.ok(list);
        }
        else{
            return ResponseEntity.badRequest().build();
        }
    }

    //@PostMapping("/detail")
    //public Optional<VideoDTO> read(VideoDTO vo) {
    //    Optional<VideoDTO> one = videoService.findById(vo.getVideo_id());
    //    System.out.println("controller result>> " + one);
    //    return one;
    //}

    @PostMapping ("/upload")
    public Object save(HttpServletRequest request, MultipartFile file) throws Exception{
        System.out.println("(Controller) insert 요청");
        String savedName0 = file.getOriginalFilename();
        String randomUUID = UUID.randomUUID().toString(); // 랜덤한 UUID 생성
        String savedName = randomUUID + savedName0;

        System.out.println(savedName);
        VideoDTO vo = new VideoDTO();

        // JWT 토큰에서 이메일 추출
        String jwt = request.getHeader("Authorization");
        jwt = jwt.replace("Bearer ", ""); // "Bearer " 접두사 제거
        if(validateJWT(jwt)) {
            String email = extractEmailFromJWT(jwt); // JWT 토큰에서 이메일 추출하는 함수 호출
            System.out.println(email);

            // 현재 시간 가져오기
            LocalDateTime currentTime = LocalDateTime.now();

            // 파일 업로드 서비스를 통해 파일 업로드
            String uploadedFileName = fileUploadService.uploadFile(file, savedName,"upload");

            vo.setVideo_link(savedName);
            vo.setVideo_name(savedName0);
            vo.setUpload_time(currentTime);
            vo.setDelete_state(0);
            vo.setEmail(email);
            System.out.println(vo);
            VideoDTO vo2 = videoService.save(vo);

            // 소리가 제거된 영상을 업로드
            //File mutedVideoFile = fileUploadService.removeAudioFromVideo(file);
            //String uploadedMutedFileName = fileUploadService.uploadFile2(mutedVideoFile, savedName, "upload-mute");

            // 소리 파일을 업로드
            //File soundFile = fileUploadService.extractAudioFromVideo(file);
            //String uploadedSoundFileName = fileUploadService.uploadFile2(soundFile, savedName, "upload-sound");

            return vo2;
            //model.addAttribute("result", vo2);
        }
        else{
            System.out.println("인증 실패");
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update/delete_state/{video_id}")
    public VideoDTO updateDeleteState(@PathVariable("video_id") int video_id) {
        VideoDTO updatedVideo = videoService.updateDeleteState(video_id);
        return updatedVideo;
    }

}

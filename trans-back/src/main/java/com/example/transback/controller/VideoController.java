package com.example.transback.controller;

import com.example.transback.VideoVO;
import com.example.transback.service.FileUploadService;
import com.example.transback.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class VideoController {
    @Autowired
    VideoService videoService;

    private final FileUploadService fileUploadService;

    @Autowired
    public VideoController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @GetMapping("/test")
    public String list() {
        return "index";
    }


    @GetMapping("videoList")
    public List<VideoVO> findAll() {
        List<VideoVO> list = videoService.findAll();
        System.out.println("controller result>> " + list);
        //model.addAttribute("list", list);
        return list;
    }

    @GetMapping("videoDetail")
    public Optional<VideoVO> read(VideoVO vo) {
        Optional<VideoVO> one = videoService.findById(vo.getVideo_id());
        System.out.println("controller result>> " + one);
        //model.addAttribute("vo", one);
        return one;
    }

    @PostMapping ("/upload/video")
    public VideoVO save(HttpServletRequest request, MultipartFile file) throws Exception{

        System.out.println("(Controller) insert 요청");
        String time = CurrentDate() + CurrentTime();
        String savedName0 = file.getOriginalFilename();
        String savedName = time + savedName0;
        System.out.println(savedName);

//        String uploadPath = request.getSession().getServletContext().getRealPath("/upload");
//        File target = new File(uploadPath + "/" + savedName);
//        file.transferTo(target);

        // 파일 업로드 서비스를 통해 파일 업로드
        String uploadedFileName = fileUploadService.uploadFile(file,savedName);

        VideoVO vo = new VideoVO();
        vo.setVideo_link(savedName);
        System.out.println(vo);
        VideoVO vo2 = videoService.save(vo);
        //model.addAttribute("result", vo2);
        return vo2;
    }


    // 현재 시간
    public String CurrentTime() {

        // 현재 시간
        LocalTime now = LocalTime.now();
//		System.out.println(now); // 06:20:57.008731300
        // 포맷 정의하기
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("-HHmmss");
        // 포맷 적용하기
        String formatedNow = now.format(formatter);
        return formatedNow;
    }

    // 오늘 날짜
    public Date CurrentDate() {
        // 현재 날짜 구하기
        LocalDate now = LocalDate.now();
        // 포맷 정의
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // 포맷 적용
        String formatedNow = now.format(formatter);
        Date today = Date.valueOf(formatedNow);
        return today;
    }
}

package com.example.transback.controller;

import com.example.transback.dto.MailDTO;
import com.example.transback.dto.VideoDTO;
import com.example.transback.service.FileUploadService;
import com.example.transback.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

import static com.example.transback.util.JwtUtil.extractEmailFromJWT;
import static com.example.transback.util.JwtUtil.validateJWT;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Qualifier;

@RestController
//@RefreshScope
//@EnableAsync
@RequestMapping("/video")
public class VideoController {

    @Autowired
    VideoService videoService;

    private final RestTemplate restTemplate;
    private final FileUploadService fileUploadService;

    @Value("${aiApi}")
    private String aiApi;

    @Value("${email.url}")
    private String backendUrl;

    @Autowired
    public VideoController(RestTemplate restTemplate, FileUploadService fileUploadService) {
        this.restTemplate = restTemplate;
        this.fileUploadService = fileUploadService;
    }

    // AI 서버 응답 데이터 클래스
    private static class AIResponse {
        private String status;
        private String result;

        public String getResult() {
            return result;
        }
    }

    @GetMapping("/list")
    public List<VideoDTO> findAll() throws InterruptedException {
        Thread.sleep(10000); // 10초 대기
        List<VideoDTO> list = videoService.findAll();
        System.out.println("controller result>> " + list);
        //model.addAttribute("list", list);
        return list;
    }

    @PostMapping("/list/email")
    public ResponseEntity<List<VideoDTO>> findVideosByEmailAndDeleteZero(HttpServletRequest request) throws Exception{
        // JWT 토큰에서 이메일 추출
        String jwt = request.getHeader("Authorization");
        jwt = jwt.replace("Bearer ", ""); // "Bearer " 접두사 제거
        //System.out.println(jwt);
        Thread.sleep(10000);


        String email = extractEmailFromJWT(jwt); // JWT 토큰에서 이메일 추출하는 함수 호출
        System.out.println(email);
        List<VideoDTO> list = videoService.findVideosByEmailAndDeleteZero(email);
        //System.out.println("controller result>> " + list);
        return ResponseEntity.ok(list);

    }


    // 요청을 저장하기 위한 어레이 (최대 2개)
//    private List<MultipartFile> requestArray = new ArrayList<>();
    private int count = 0;
    private Queue<HttpServletRequest> requestQueue = new LinkedList<>();
    // 요청을 저장하기 위한 큐 (2개 초과 요청 저장)
//    private BlockingQueue<MultipartFile> requestQueue = new ArrayBlockingQueue<>(Integer.MAX_VALUE);


    @PostMapping("/upload")
    public ResponseEntity<String> save0(HttpServletRequest request) throws Exception {
        // 어레이에 저장
//        requestArray.add(file);

        String emailUrl = "http://localhost:9092/video/list/email";

        if (count < 1) {
            System.out.println("************************************************");
            count = count+1;
            HttpHeaders headers = new HttpHeaders();
            String jwt = request.getHeader("Authorization");
            headers.set("Authorization", jwt); // JWT 토큰을 헤더에 설정

            HttpEntity<String> httpEntity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(emailUrl, HttpMethod.POST, httpEntity, String.class);
            System.out.println(response);
            System.out.println("현재 큐에 대기 중인 요청 개수: " + requestQueue.size());
            count =count-1;
//          return response;
//            processRequestQueue();

        }
        else{
            System.out.println("************************************************");
//            requestQueue.add(request);
            requestQueue.add(new HttpServletRequestWrapper(request));

            System.out.println("큐 내용: " + requestQueue.toString());
            HttpServletRequest firstRequest = requestQueue.peek();
            String jwt = firstRequest.getHeader("Authorization");
            System.out.println("큐에서 꺼낸 jwt: " + jwt);


            // 큐에 대기 중인 요청 개수 반환
            return ResponseEntity.ok("00현재 큐에 대기 중인 요청 개수: " + requestQueue.size());

        }
        return ResponseEntity.ok("현재 큐에 대기 중인 요청 개수: "+requestQueue.size());
    }

    // 큐 처리를 위한 메소드
//    @PostMapping("/upload")
//    public ResponseEntity<String> save0(HttpServletRequest request) throws Exception
    @PostMapping("/upload2")
    public ResponseEntity<String> save1(HttpServletRequest request) throws Exception {
        String emailUrl = "http://localhost:9092/video/list/email";

        while (!requestQueue.isEmpty()) {
            System.out.println("************************************************");
            System.out.println("큐 내용: " + requestQueue.toString());
            System.out.println(requestQueue.size());

            HttpServletRequest queuedRequest = requestQueue.poll();
            System.out.println(requestQueue.size());

            count += 1;

            String jwt2 = queuedRequest.getHeader("Authorization");

            // 전체 요청 정보 출력
            System.out.println("큐에서 꺼낸 요청 정보:");
            System.out.println("Method: " + queuedRequest.getMethod());
            System.out.println("URI: " + queuedRequest.getRequestURI());
            System.out.println("Headers: " + Collections.list(queuedRequest.getHeaderNames()));
            System.out.println("JWT: " + jwt2);

            System.out.println("큐에 jwt2:" + jwt2);

            HttpHeaders headers2 = new HttpHeaders();
            headers2.set("Authorization", jwt2); // JWT 토큰을 헤더에 설정

            HttpEntity<String> httpEntity2 = new HttpEntity<>(headers2);
            ResponseEntity<String> response2 = restTemplate.exchange(emailUrl, HttpMethod.POST, httpEntity2, String.class);
            System.out.println(response2);
            System.out.println("현재 큐에 대기 중인 요청 개수: " + requestQueue.size());
            count -= 1;
        }
        return ResponseEntity.ok("현재 큐에 대기 중인 요청 개수: "+requestQueue.size());
    }

    @PostMapping ("/upload0")
    public ResponseEntity<String> save(HttpServletRequest request, MultipartFile file) throws Exception{
        System.out.println("(Controller) insert 요청");
        String savedName0 = file.getOriginalFilename();
        String randomUUID = UUID.randomUUID().toString(); // 랜덤한 UUID 생성
        String savedName = randomUUID + savedName0;

        System.out.println(savedName);
        VideoDTO vo = new VideoDTO();

        // JWT 토큰에서 이메일 추출
        String jwt = request.getHeader("Authorization");
        jwt = jwt.replace("Bearer ", ""); // "Bearer " 접두사 제거

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

        // 서명된 URL 생성
        long expirationTimeInMilliseconds = 604800000;
        String signedURL = fileUploadService.generateSignedURL(savedName,"upload", expirationTimeInMilliseconds);
        //System.out.println("upload_url: "+signedURL);
        vo.setUpload_url(signedURL);
        System.out.println(vo);


        //ai 서버 생략 (테스트할때만)
        Thread.sleep(10000);
        return ResponseEntity.ok(signedURL);

            // AI 서버의 API에 요청
//            String aiServerUrl = aiApi + savedName;
//            System.out.println(aiServerUrl);
//            ResponseEntity<AIResponse> aiResponse = restTemplate.postForEntity(aiServerUrl, null, AIResponse.class);
//            // AI 서버의 응답 데이터 처리
//            if (aiResponse != null && aiResponse.getStatusCode().is2xxSuccessful()) {
//                AIResponse responseBody = aiResponse.getBody();
//                String result = responseBody.getResult();
//                //System.out.println("AI Server Response: " + result);
//                //long expirationTimeInMilliseconds2 = 3600000;
//                String signedURL2 = fileUploadService.generateSignedURL2(result, expirationTimeInMilliseconds);
//                vo.setOutput_url(signedURL2);
//                videoService.save(vo);
//
//                MailDTO mailDto = new MailDTO();
//                mailDto.setAddress(email);  // 이메일 주소 설정
//                mailDto.setTitle("Video Uploaded");
//                mailDto.setContent("Your video has been successfully uploaded!");
//                System.out.println("비디오 컨트롤러:"+mailDto);
//                String emailUrl = backendUrl + "/email/send";
//
//                // 이메일 전송 요청
//                ResponseEntity<String> response = restTemplate.postForEntity(emailUrl, mailDto, String.class);
//
//                return ResponseEntity.ok(signedURL2);
//
//            } else {
//                // AI 서버 요청 실패 처리
//                return ResponseEntity.badRequest().build();
//            }


    }

    @PutMapping("/update/delete_state/{video_id}")
    public VideoDTO updateDeleteState(@PathVariable("video_id") int video_id) {
        VideoDTO updatedVideo = videoService.updateDeleteState(video_id);
        return updatedVideo;
    }

}

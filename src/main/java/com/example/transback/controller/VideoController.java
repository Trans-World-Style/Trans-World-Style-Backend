package com.example.transback.controller;

import com.example.transback.dto.FileInfo;
import com.example.transback.dto.MailDTO;
import com.example.transback.dto.QueueDTO;
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
import java.util.concurrent.ConcurrentLinkedQueue;
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
        String email = extractEmailFromJWT(jwt); // JWT 토큰에서 이메일 추출하는 함수 호출
        System.out.println(email);
        List<VideoDTO> list = videoService.findVideosByEmailAndDeleteZero(email);
        //System.out.println("controller result>> " + list);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/list/email/wait")
    public ResponseEntity<List<VideoDTO>> findVideosByEmailAndDeleteZero2(HttpServletRequest request) throws Exception{
        // JWT 토큰에서 이메일 추출
        String jwt = request.getHeader("Authorization");
        jwt = jwt.replace("Bearer ", ""); // "Bearer " 접두사 제거
        //System.out.println(jwt);
        String email = extractEmailFromJWT(jwt); // JWT 토큰에서 이메일 추출하는 함수 호출
        System.out.println(email);
        List<VideoDTO> list = videoService.findVideosByEmailAndDeleteZeroWait(email);
        System.out.print("wait list: "+list);
        for(VideoDTO video:list){
            int id=video.getVideo_id();
            String video_link=video.getVideo_link();
            int[] result=findVideoIndex(video_link);
            int rank=result[0];
            int time=result[1];
            videoService.updateWaitingRank(id,rank,time);
        }
        System.out.print("wait list2: "+list);

        List<VideoDTO> list2 = videoService.findVideosByEmailAndDeleteZeroWait(email);
        //System.out.println("controller result>> " + list);
        return ResponseEntity.ok(list2);
    }

    private int[] findVideoIndex(String filename) {
        int index = 0;
        int total=0;
        int[] result = new int[2];
        for (FileInfo fileInfo : queueDTO.getFileInfoQueue()) {
            System.out.println("큐순위 카운트: " + fileInfo.getFilename()); // fileInfo.getFilename()을 사용하여 파일 이름 얻음
            System.out.println("큐순위 카운트: " + fileInfo.getFileSize()); // fileInfo.getFilename()을 사용하여 파일 이름 얻음
            System.out.println("큐순위 카운트 file name: " + filename);
            index++;
            total+=(int)fileInfo.getFileSize()/10000;

            if (fileInfo.getFilename().equals(filename)) { // fileInfo.getFilename()으로 파일 이름 비교

                result[0] = index;
                result[1] = (int) total;
                return result;
            }
        }
        result[0]=0;
        result[1]=0;
        return result;
    }



    private int count = 0;
    private QueueDTO queueDTO = new QueueDTO();

    @PostMapping("/upload")
    public ResponseEntity<String> save0(HttpServletRequest request,MultipartFile file) throws Exception {

        String jwt = request.getHeader("Authorization");
        String video_link=uploadOriginal(jwt,file);  //s3에 원본 upload
        long videoSize = file.getSize();


        if (count <2) {
            System.out.println("************************************************");
            processThread(video_link);     //현재 thread 업스케일링 실행
            processQueue();      //남은 큐 업스케일링 실행

        } else {
            System.out.println("************************************************");
            queueDTO.addFile(video_link,videoSize);
            int[] array=findVideoIndex(video_link);
            int index=array[0];
            int waiting=array[1];
            System.out.println("큐 대기 순위:"+index);
            int video_id=videoService.findVideosByVideoLink(video_link);
            videoService.updateWaitingRank(video_id,index,waiting);

            // 큐에 대기 중인 요청 개수 반환
            return ResponseEntity.ok("00현재 큐에 대기 중인 요청 개수: " + queueDTO.size());
        }

        return ResponseEntity.ok("현재 큐에 대기 중인 요청 개수: " + queueDTO.size());
    }


    private String uploadOriginal(String jwt, MultipartFile file) throws Exception{
        System.out.println("(Controller) insert 요청");
        String savedName0 = file.getOriginalFilename();
        String randomUUID = UUID.randomUUID().toString(); // 랜덤한 UUID 생성
        String savedName = randomUUID + savedName0;
        long videoSize = file.getSize();


        System.out.println(savedName);
        VideoDTO vo = new VideoDTO();

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
        vo.setUpscale_state(0);
        vo.setWaiting_rank(0);
        vo.setWaiting_time(0);

        // 서명된 URL 생성
        long expirationTimeInMilliseconds = 604800000;
        String signedURL = fileUploadService.generateSignedURL(savedName,"upload", expirationTimeInMilliseconds);
        //System.out.println("upload_url: "+signedURL);
        vo.setUpload_url(signedURL);
        System.out.println(vo);
        videoService.save(vo);

        return savedName;
    }


    private void processThread(String video_link) throws Exception {
        count += 1;

        // 전체 요청 정보 출력
        System.out.println("큐에서 꺼낸 요청 정보:");
        System.out.println("video_link: " + video_link);


        ResponseEntity<String> response2=aiTransfer(video_link);
//        System.out.println(response2);

        System.out.println("현재 큐에 대기 중인 요청 개수: " + queueDTO.size());
        count -= 1;
    }

    private void processQueue() throws Exception {

        while (!queueDTO.isEmpty()) {
            System.out.println("************************************************");
            System.out.println("큐 내용: " + queueDTO.toString());

            System.out.println(queueDTO.size());
            FileInfo fileInfo = queueDTO.pollFile(); // 파일 정보를 가져옴

            System.out.println(queueDTO.size());

            String filename = fileInfo.getFilename(); // 파일 이름
            long videoSize = fileInfo.getFileSize(); // 파일 크기
//            String filename,long video = queueDTO.pollFile();
            System.out.println(queueDTO.size());

            processThread(filename);
        }
    }

    private ResponseEntity<String> aiTransfer(String video_link) throws Exception{

        Thread.sleep(10000);

        int video_id=videoService.findVideosByVideoLink(video_link);

        //ai 변환 성공 후

        String aiServerUrl = aiApi + video_link;
        System.out.println(aiServerUrl);
        ResponseEntity<AIResponse> aiResponse = restTemplate.postForEntity(aiServerUrl, null, AIResponse.class);
        // AI 서버의 응답 데이터 처리
        if (aiResponse != null && aiResponse.getStatusCode().is2xxSuccessful()) {
            AIResponse responseBody = aiResponse.getBody();
            String result = responseBody.getResult();
            //System.out.println("AI Server Response: " + result);
//            long expirationTimeInMilliseconds2 = 3600000;
            long expirationTimeInMilliseconds = 604800000;
            String signedURL2 = fileUploadService.generateSignedURL2(result, expirationTimeInMilliseconds);
//            vo.setOutput_url(signedURL2);
//            videoService.save(vo);
//
//            MailDTO mailDto = new MailDTO();
//            mailDto.setAddress(email);  // 이메일 주소 설정
//            mailDto.setTitle("Video Uploaded");
//            mailDto.setContent("Your video has been successfully uploaded!");
//            System.out.println("비디오 컨트롤러:"+mailDto);
//            String emailUrl = backendUrl + "/email/send";

            // 이메일 전송 요청
//            ResponseEntity<String> response = restTemplate.postForEntity(emailUrl, mailDto, String.class);

        } else {
            // AI 서버 요청 실패 처리
            return ResponseEntity.badRequest().build();
        }


        VideoDTO updatedVideo =videoService.updateUpscaleState(video_id);
        System.out.println(updatedVideo);

        return ResponseEntity.ok(video_link);
    }
















//    @PostMapping ("/upload0")
//    public ResponseEntity<String> upload0(HttpServletRequest request, MultipartFile file) throws Exception{
//        System.out.println("(Controller) insert 요청");
//        String savedName0 = file.getOriginalFilename();
//        String randomUUID = UUID.randomUUID().toString(); // 랜덤한 UUID 생성
//        String savedName = randomUUID + savedName0;
//
//        System.out.println(savedName);
//        VideoDTO vo = new VideoDTO();
//
//        // JWT 토큰에서 이메일 추출
//        String jwt = request.getHeader("Authorization");
//        jwt = jwt.replace("Bearer ", ""); // "Bearer " 접두사 제거
//
//        String email = extractEmailFromJWT(jwt); // JWT 토큰에서 이메일 추출하는 함수 호출
//        System.out.println(email);
//
//        // 현재 시간 가져오기
//        LocalDateTime currentTime = LocalDateTime.now();
//
//        // 파일 업로드 서비스를 통해 파일 업로드
//        String uploadedFileName = fileUploadService.uploadFile(file, savedName,"upload");
//
//        vo.setVideo_link(savedName);
//        vo.setVideo_name(savedName0);
//        vo.setUpload_time(currentTime);
//        vo.setDelete_state(0);
//        vo.setEmail(email);
//
//        // 서명된 URL 생성
//        long expirationTimeInMilliseconds = 604800000;
//        String signedURL = fileUploadService.generateSignedURL(savedName,"upload", expirationTimeInMilliseconds);
//        //System.out.println("upload_url: "+signedURL);
//        vo.setUpload_url(signedURL);
//        System.out.println(vo);
//
//
//        //ai 서버 생략 (테스트할때만)
//        //Thread.sleep(10000);
//        return ResponseEntity.ok(signedURL);
//
//        // AI 서버의 API에 요청
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
//
//
//    }

    @PutMapping("/update/delete_state/{video_id}")
    public VideoDTO updateDeleteState(@PathVariable("video_id") int video_id) {
        VideoDTO updatedVideo = videoService.updateDeleteState(video_id);
        return updatedVideo;
    }

}

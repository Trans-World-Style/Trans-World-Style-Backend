package com.example.transback.controller;

import com.example.transback.dto.AuthResponse;
import com.example.transback.dto.MemberDTO;
import com.example.transback.service.MemberService;
import com.example.transback.util.JwtUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/member")
//@CrossOrigin(origins = {"http://localhost:3000","https://tw-style.duckdns.org"})
@CrossOrigin(origins = "https://tw-style.duckdns.org")
public class MemberController {

    @Autowired
    private MemberService memberService;

    private final String CLIENT_ID = "585543292084-nglvej9fqvsm7in5bgev62scqbqpnllr.apps.googleusercontent.com";

    @PostMapping("/auth")
    public ResponseEntity<?> authenticateUser(@RequestBody Map<String, String> requestBody) {
        String googleIdToken = requestBody.get("id_token");
        System.out.println(googleIdToken);
        // 구글 ID 토큰 유효성 검사 및 가져오기
        HttpTransport transport;
        JsonFactory jsonFactory;
        try {
            transport = GoogleNetHttpTransport.newTrustedTransport();
            jsonFactory = JacksonFactory.getDefaultInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

        try {
            GoogleIdToken idToken = verifier.verify(googleIdToken);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                String email = payload.getEmail();
                String name = (String) payload.get("name");

                // 회원 정보 생성
                MemberDTO memberDTO = new MemberDTO();
                memberDTO.setEmail(email); // 이메일 설정
                memberDTO.setMember_name(name); // 회원 이름 설정
                memberDTO.setGoogle_id(payload.getSubject()); // 구글 ID 설정
                memberDTO.setCreated_at(LocalDateTime.now()); // 생성일 설정
                memberDTO.setUpdated_at(LocalDateTime.now()); // 업데이트일 설정
                System.out.println("controller result>> " + memberDTO);

                // MemberService를 통해 회원 추가 또는 인증 처리
                memberService.processGoogleLogin(memberDTO);

                // 토큰이 유효하면 JWT 생성 및 반환

                String jwt = JwtUtil.generateJWT(email);
                return ResponseEntity.ok(new AuthResponse(jwt));
            } else {
                // 유효하지 않은 토큰
                return ResponseEntity.badRequest().build();
            }
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}
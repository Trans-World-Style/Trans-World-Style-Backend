package com.example.transback.controller;

import com.example.transback.dto.MemberDTO;
import com.example.transback.service.MemberService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.core.type.TypeReference;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("-")
    public void test1(){

    }

//    @Test
//    @DisplayName("Test authenticateUser")
//    public void testAuthenticateUser() throws Exception {
//        String googleIdToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6ImZkNDhhNzUxMzhkOWQ0OGYwYWE2MzVlZjU2OWM0ZTE5NmY3YWU4ZDYiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI1ODU1NDMyOTIwODQtbmdsdmVqOWZxdnNtN2luNWJnZXY2MnNjcWJxcG5sbHIuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI1ODU1NDMyOTIwODQtbmdsdmVqOWZxdnNtN2luNWJnZXY2MnNjcWJxcG5sbHIuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMDQ1MTk5ODIwNDAyMTYzODYzNDEiLCJlbWFpbCI6Inp6eHg5NjMzQGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJuYmYiOjE2OTA4Nzg0NzQsIm5hbWUiOiLslYjsoJXsmrAiLCJwaWN0dXJlIjoiaHR0cHM6Ly9saDMuZ29vZ2xldXNlcmNvbnRlbnQuY29tL2EvQUFjSFR0ZVUySHVMMFlXbENQaGNOS3RJbERtVDZOOVNNMEpKcXZnRDdVRlNHb3VVPXM5Ni1jIiwiZ2l2ZW5fbmFtZSI6IuygleyasCIsImZhbWlseV9uYW1lIjoi7JWIIiwibG9jYWxlIjoia28iLCJpYXQiOjE2OTA4Nzg3NzQsImV4cCI6MTY5MDg4MjM3NCwianRpIjoiOTI0ODgzODM5NmJmMWY3YmU0MzQyZDgxZWQ3MjJkZjI3NTA4MTM0ZCJ9.eyIoUrm1ItUi0zf4z9AcE3Zi-iq9LjtTuzPjyg_cuHupmHtc0vYR528uzguprlUb1OHXquQ2M7LTUih_ThRqM3xq2F4sD5XmmDi4RUffpt2-H5WmcLJFVzCrPbc4NGtSiJEXgZ9Q8wGDptSC6g_bUESWnX_TQrQYZszJL_Hi695dAo6yDAK9BPwwTF3j5NITXUgCKT28o5LBW4MrDmqrSRZOCKmTKgd3t3u-KABxjjHT2_PpgJKfLHyeQO9FasJpJhakyYMBCPCCclZ7agyiziGSYjBIi5fRdLrKctf2joUGSxkI6oKUUFxmypfoUH-s31DhaoDYEYfaKER3PpLlOw"; // 테스트용 Google ID 토큰
//
//        Map<String, String> requestBody = new HashMap<>();
//        requestBody.put("id_token", googleIdToken);
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/member/auth")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(requestBody)))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.cookie().exists("jwt")) // JWT 쿠키가 존재하는지 확인
//                .andDo(print());
//    }

    // 객체를 JSON 문자열로 변환하는 메서드
    private String asJsonString(Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

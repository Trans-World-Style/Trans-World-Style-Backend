package com.example.transback.controller;

import com.example.transback.dto.AuthResponse;
import com.example.transback.dto.MemberDTO;
import com.example.transback.service.MemberService;

import com.example.transback.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.mockito.BDDMockito.given;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.core.type.TypeReference;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MemberController.class)
@ActiveProfiles("test") // 테스트 환경용 프로파일로 설정
public class MemberControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @BeforeEach
    public void setup() {
        // 테스트에 필요한 Mock 객체들의 동작을 설정
        // memberService.processGoogleLogin() 메서드 호출 시 아무 동작도 하지 않도록 설정
//        given(memberService.processGoogleLogin(any(MemberDTO.class))).willReturn(null);
        doNothing().when(memberService).processGoogleLogin(any(MemberDTO.class));
    }

    @Test
    @DisplayName("사용자 인증 API 테스트")
    public void testAuthenticateUser() throws Exception {
        // 가짜 요청 바디 생성
        Map<String, String> requestBody = new HashMap<>();
        String id_token="eyJhbGciOiJSUzI1NiIsImtpZCI6ImEzYmRiZmRlZGUzYmFiYjI2NTFhZmNhMjY3OGRkZThjMGIzNWRmNzYiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJuYmYiOjE2ODk3NTg4NDcsImF1ZCI6IjU4NTU0MzI5MjA4NC1uZ2x2ZWo5ZnF2c203aW41YmdldjYyc2NxYnFwbmxsci5hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsInN1YiI6IjEwNDUxOTk4MjA0MDIxNjM4NjM0MSIsImVtYWlsIjoienp4eDk2MzNAZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsImF6cCI6IjU4NTU0MzI5MjA4NC1uZ2x2ZWo5ZnF2c203aW41YmdldjYyc2NxYnFwbmxsci5hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsIm5hbWUiOiLslYjsoJXsmrAiLCJwaWN0dXJlIjoiaHR0cHM6Ly9saDMuZ29vZ2xldXNlcmNvbnRlbnQuY29tL2EvQUFjSFR0ZVUySHVMMFlXbENQaGNOS3RJbERtVDZOOVNNMEpKcXZnRDdVRlNHb3VVPXM5Ni1jIiwiZ2l2ZW5fbmFtZSI6IuygleyasCIsImZhbWlseV9uYW1lIjoi7JWIIiwiaWF0IjoxNjg5NzU5MTQ3LCJleHAiOjE2ODk3NjI3NDcsImp0aSI6ImQ2ZmEyMTU3NmRhZWFmMTNiYWJlNTNkYzY0Y2UwNzA0NzEyZWEwOTkifQ.MkJexRFo5LCDNvyQpg5n2iLRbhbFD9qGWPJBbDsdFifNYKV4B-1rkybpjDpij-zPf4uMJg41LqwxbPyQEp2PYsouVj8w8rAeAMGZskuX5g9KpQM8b616pTrXLTLdzd0A3pxm8t8lC7hiGZexfSfNnNRmn1zjpWmadHAYpdBonL0qR2w3-3vfLvWUs1bVSl1sSX4eKLTcZuAVTDs2W66F9bcU5ErN_ZQ5bHd3gXw-F6L8a86gavaCxNtbrILuN4yCE9RevF7jw6VefxQQr4F5UsPULl51giWMssH1wILlu9-Nd0KL-nD-LgQN-yTo9ndfp5Z2e7OeXuu1k2NGbgwZyA";
        requestBody.put("id_token", id_token);
        // 가짜 응답 데이터 생성
//        String email = "test@example.com";
//        String jwt = JwtUtil.generateJWT(email);
//        AuthResponse expectedResponse = new AuthResponse(jwt);

        // MockMvc를 사용하여 API 요청 및 응답 검증
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/member/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id_token\":\"" + id_token + "\"}"))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.jwt").exists())
                        .andReturn(); // 응답 결과 가져오기

        // JSON 응답에서 jwt 추출
        String responseString = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> jsonMap = objectMapper.readValue(responseString, new TypeReference<Map<String, String>>() {});

        String jwtToken = jsonMap.get("jwt");
        System.out.println("jwt:"+jwtToken);

        // MemberService의 processGoogleLogin() 메서드가 올바르게 호출되었는지 확인
        verify(memberService, times(1)).processGoogleLogin(any(MemberDTO.class));
    }

}

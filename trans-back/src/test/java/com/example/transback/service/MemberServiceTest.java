package com.example.transback.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.transback.dao.MemberDAOInterface;
import com.example.transback.dto.MemberDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @MockBean
    private MemberDAOInterface memberDAOInterface;

    @Test
    void processGoogleLogin() {
        // 가짜 MemberDTO 객체 생성
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setEmail("test@example.com");

        // MemberDAOInterface의 findByEmail() 메소드가 존재하지 않는 회원을 반환하도록 Mock 설정
        when(memberDAOInterface.findByEmail(any())).thenReturn(Optional.empty());

        // MemberDAOInterface의 save() 메소드가 호출되는지 확인
        memberService.processGoogleLogin(memberDTO);
        verify(memberDAOInterface, times(1)).save(any());
    }

    @Test
    @DisplayName("processGoogleLogin() 메소드 - 이미 존재하는 회원인 경우")
    public void testProcessGoogleLoginExistingMember() {
        // 가짜 MemberDTO 객체 생성
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setEmail("test@example.com");

        // 존재하는 회원을 반환하는 Optional 객체 생성
        MemberDTO existingMember = new MemberDTO();
        existingMember.setEmail("test@example.com");
        Optional<MemberDTO> optionalMember = Optional.of(existingMember);

        // MemberDAOInterface의 findByEmail() 메소드가 존재하는 회원을 반환하도록 Mock 설정
        when(memberDAOInterface.findByEmail(any())).thenReturn(optionalMember);

        // MemberDAOInterface의 save() 메소드가 호출되지 않는지 확인
        memberService.processGoogleLogin(memberDTO);
        verify(memberDAOInterface, times(0)).save(any());
    }
}
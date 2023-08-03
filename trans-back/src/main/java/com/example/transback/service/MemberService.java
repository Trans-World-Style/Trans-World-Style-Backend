package com.example.transback.service;

import com.example.transback.dao.MemberDAOInterface;
import com.example.transback.dto.MemberDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    private MemberDAOInterface memberDAOInterface;

    @Transactional(readOnly = false)
    public void processGoogleLogin(MemberDTO memberDTO) {
        String email = memberDTO.getEmail();
        // 이메일로 회원 조회
        Optional<MemberDTO> existingMember = memberDAOInterface.findByEmail(email);
        if (existingMember.isPresent()) {
            // 이미 회원이 존재하는 경우
        } else {
            // 회원이 존재하지 않는 경우
            memberDAOInterface.save(memberDTO);
        }
    }

}

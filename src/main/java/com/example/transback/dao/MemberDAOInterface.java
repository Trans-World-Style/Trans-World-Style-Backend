package com.example.transback.dao;

import com.example.transback.dto.MemberDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberDAOInterface extends JpaRepository<MemberDTO, Integer> {
    Optional<MemberDTO> findByEmail(String email);
}

package com.example.transback.dao;

import com.example.transback.dto.VideoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VideoDAOInterface extends JpaRepository<VideoDTO, Integer> {
    @Query("SELECT v FROM video v WHERE v.delete_state = 0 AND v.email = :email")
    List<VideoDTO> findVideosByEmailAndDeleteZero(@Param("email") String email);

    @Query("SELECT v FROM video v WHERE v.delete_state = 0 AND v.upload_time <= :uploadTime")
    List<VideoDTO> findVideosByUploadTimeBeforeAndDeleteStateIsZero(LocalDateTime uploadTime);

}

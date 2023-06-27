package com.example.transback;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoDAOInterface extends JpaRepository<VideoVO, Integer> {
}

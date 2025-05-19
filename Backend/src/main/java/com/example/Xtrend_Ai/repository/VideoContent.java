package com.example.Xtrend_Ai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoContent extends JpaRepository<VideoContent, Long> {
}

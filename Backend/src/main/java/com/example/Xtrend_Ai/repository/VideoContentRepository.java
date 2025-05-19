package com.example.Xtrend_Ai.repository;

import com.example.Xtrend_Ai.entity.VideoContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoContentRepository extends JpaRepository<VideoContent, Long> {
}

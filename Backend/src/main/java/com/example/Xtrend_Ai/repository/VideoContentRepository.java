package com.example.Xtrend_Ai.repository;

import com.example.Xtrend_Ai.entity.VideoContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoContentRepository extends JpaRepository<VideoContent, Long> {


    @Query("SELECT v FROM VideoContent v where v.video_id=?1 ")
    Optional<VideoContent> findVideoContentByVideoId(String videoId);
}

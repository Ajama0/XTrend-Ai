package com.example.Xtrend_Ai.repository;

import com.example.Xtrend_Ai.entity.Podcast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PodcastRepository extends JpaRepository<Podcast, Long> {


    @Query("SELECT p FROM Podcast p WHERE p.user.email = ?1 AND (p.status = 'COMPLETED' OR p.status = 'PROCESSING')")
    List<Podcast> findUserPodcasts(String email);
}

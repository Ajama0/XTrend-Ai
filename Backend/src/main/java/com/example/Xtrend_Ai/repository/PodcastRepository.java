package com.example.Xtrend_Ai.repository;

import com.example.Xtrend_Ai.entity.Podcast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PodcastRepository extends JpaRepository<Podcast, Long> {
}

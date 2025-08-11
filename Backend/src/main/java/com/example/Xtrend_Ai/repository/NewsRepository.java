package com.example.Xtrend_Ai.repository;

import com.example.Xtrend_Ai.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    @Query("SELECT * FROM NEWS n order by n.pubDate")
    List<News> findAllArticlesByDate();
}

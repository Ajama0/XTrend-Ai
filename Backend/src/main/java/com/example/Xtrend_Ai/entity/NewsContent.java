package com.example.Xtrend_Ai.entity;

import com.example.Xtrend_Ai.enums.Topics;
import com.example.Xtrend_Ai.enums.Types;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Service;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Service
@Builder
@ToString
public class NewsContent extends Content {

    /**
     * This class contains the news Content(article/blog) returned by Open Ai API which reflects the personalized content
     */

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String author;


    @Enumerated(EnumType.STRING)
    private Topics topic;

    @ManyToOne
    @JoinColumn(name = "news_id")
    private News news;



}

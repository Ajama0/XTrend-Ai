package com.example.Xtrend_Ai.entity;

import com.example.Xtrend_Ai.enums.ContentForm;
import com.example.Xtrend_Ai.enums.PodcastType;
import com.example.Xtrend_Ai.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Podcast {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Status status;


    private String key;


    private ContentForm contentForm;

    @Column(name="created_at")
    private String date = LocalDateTime.now().toString();


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToOne
    @JoinColumn(name="news_id")
    private News news;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PodcastType podcastType;
}

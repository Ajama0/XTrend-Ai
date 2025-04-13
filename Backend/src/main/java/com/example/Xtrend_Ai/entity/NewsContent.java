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
public class NewsContent {

    @SequenceGenerator(sequenceName = "newsContent_sequence",
            name = "newsContent_sequence",
            allocationSize = 1
    )

    @GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "newsContent_sequence"
    )

    @Id
    private Long id;


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

    @ManyToOne
    @JoinColumn(name="user_id",nullable = false)
    private User user;



}

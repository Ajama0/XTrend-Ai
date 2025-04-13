package com.example.Xtrend_Ai.entity;

import com.example.Xtrend_Ai.enums.Types;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
public class News {
    /**
     * This class stores all the news returned by the news API
     * , which is served to the client in 'Trending' section
     */
    @SequenceGenerator(sequenceName = "news_sequence",
            name = "news_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "news_sequence")
    @Id
    private Long id;


    @Column(name="articles", nullable = false)
    @Lob
    private List<Article> articles;

}

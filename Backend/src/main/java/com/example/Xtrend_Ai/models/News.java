package com.example.Xtrend_Ai.models;


import com.example.Xtrend_Ai.enums.Types;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.net.URL;
import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
public class News{

    /**
     * This class stores all the news returned by the news API, which is served to the client in 'Trending' section
     */
    @SequenceGenerator(sequenceName = "news_sequence",
        name = "news_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "news_sequence")
    @Id
    private Long id;

    @Column(nullable = false)
    private URL image;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Lob   /// able to store larger objects
    private String content;

    private String author;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Types type;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    /// This represents the date of the article, (when the article was created)
    private LocalDateTime ArticleDate;



}

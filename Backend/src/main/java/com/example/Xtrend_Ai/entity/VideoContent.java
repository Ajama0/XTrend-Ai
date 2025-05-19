package com.example.Xtrend_Ai.entity;

import jakarta.persistence.*;
import lombok.*;

import java.net.URL;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class VideoContent{


    @SequenceGenerator(sequenceName = "videoContent_sequence",
            name = "videoContent_sequence",
            allocationSize = 1
    )

    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "videoContent_sequence"
    )
    @Id
    private Long id;

    private Long video_id;

    private String videoTitle;


    /***
     * each generated content will be associated with the news entity.
     */
    @ManyToOne
    @JoinColumn(name = "news_id",   nullable = false)
    private News news;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

}

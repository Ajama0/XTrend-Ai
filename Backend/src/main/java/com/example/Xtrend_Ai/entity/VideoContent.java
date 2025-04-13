package com.example.Xtrend_Ai.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.net.URL;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class VideoContent extends Content {


    private URL videoUrl;

    private String videoTitle;

    private String videoDescription;


    /***
     * each generated content will be associated with the news entity.
     */
    @ManyToOne
    @JoinColumn(name = "news_id")
    private Article news;


}

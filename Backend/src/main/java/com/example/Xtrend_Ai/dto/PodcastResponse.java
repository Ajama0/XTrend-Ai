package com.example.Xtrend_Ai.dto;

import com.example.Xtrend_Ai.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.net.URL;
import java.util.List;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PodcastResponse {


    /// use this response to allow client to poll the backend

    private Long podcastId;
    private String key;
    private Status status;

    private String url;

    private String title;
    private String description;
    private String imageUrl;
    private List<String> country;
    private List<String>category;

    private String podcastTagline;



}

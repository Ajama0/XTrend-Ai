package com.example.Xtrend_Ai.utils;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Article {


    private String uuid;
    private String title;
    private String description;
    private String keywords;
    private String snippet;
    private String url;

    @JsonProperty("image_url")
    private String imageUrl;
    private String language;

    @JsonProperty("published_at")

    private String publishedAt;
    private String source;
    private List<String> categories;

    @JsonProperty("relevance_score")
    private Double relevanceScore;

    private String locale;

}

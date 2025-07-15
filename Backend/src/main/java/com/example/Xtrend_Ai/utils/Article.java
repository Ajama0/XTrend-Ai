package com.example.Xtrend_Ai.utils;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.util.List;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Article {


    private String article_id;
    private String title;


    private List<String> keywords;
    private String description;

    private String content;
    private String link;

    private String image_url;


    private String source_id;
    private String source_name;
    private String source_icon;
    private String source_url;

    private String pubDate;

    private String language;
    private List<String>country;
    private List<String> category;

    private String sentiment;







}

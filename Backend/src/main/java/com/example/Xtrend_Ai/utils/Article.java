package com.example.Xtrend_Ai.utils;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.Nullable;

import java.util.List;

@Embeddable
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Article {


    private String article_id;
    private String title;

    @ElementCollection
    private List<String> keywords;



    @Lob
    @Column(columnDefinition = "TEXT")
    private String link;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String image_url;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    private String source_id;
    private String source_name;
    private String source_icon;
    private String source_url;

    private String pubDate;

    private String language;

    @ElementCollection
    private List<String>country;

    @ElementCollection
    private List<String> category;









}

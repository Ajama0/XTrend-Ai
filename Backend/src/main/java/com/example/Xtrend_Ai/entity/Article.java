package com.example.Xtrend_Ai.entity;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Article {


    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "source_id")),
            @AttributeOverride(name = "name", column = @Column(name = "source_name"))
    })
    private Source source;

    @JsonProperty("author")
    private String author;

    @JsonProperty("title")
    @Lob
    private String title;


    @JsonProperty("description")
    @Lob
    private String description;

    @JsonProperty("url")
    @Lob
    private String url;


    @JsonProperty("urlToImage")
    @Lob
    private String urlToImage;

    @JsonProperty("publishedAt")
    private String publishedAt;

    @JsonProperty("content")
    @Lob
    private String content;
}

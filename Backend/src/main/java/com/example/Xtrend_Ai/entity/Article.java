package com.example.Xtrend_Ai.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Lob;

@Embeddable
public class Article {


    @Embedded
    private Source source;

    private String author;

    @Lob
    private String title;


    @Lob
    private String Description;
    @Lob
    private String Url;

    @Lob
    private String content;
    @Lob
    private String urlToImage;
    private String publishAt;
}

package com.example.Xtrend_Ai.models;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class NewsContent extends Content {

    /**
     * This class contains the news Content(article/blog) returned by Open Ai API which reflects the personalized content
     */



    @ManyToOne
    @JoinColumn(name = "news_id")
    private News news;
}

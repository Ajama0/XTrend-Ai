package com.example.Xtrend_Ai.mapper;

import com.example.Xtrend_Ai.dto.NewsDTO;
import com.example.Xtrend_Ai.entity.News;
import com.example.Xtrend_Ai.utils.Article;

import java.util.ArrayList;
import java.util.List;

public class NewsMapper {


    public static NewsDTO mapNewsToDto(List<News> newsResults) {
        List<Article> articles = new ArrayList<>();

        for(News news: newsResults ) {

            Article article = Article.builder()
                    .link(news.getArticle().getLink())
                    .title(news.getArticle().getTitle())
                    .country(news.getArticle().getCountry())
                    .description(news.getArticle().getDescription())
                    .image_url(news.getArticle().getImage_url())
                    .pubDate(news.getArticle().getPubDate())
                    .category(news.getArticle().getCategory())
                    .source_name(news.getArticle().getSource_name())
                    .source_icon(news.getArticle().getSource_icon())
                    .language(news.getArticle().getLanguage())
                    .newsId(news.getId())
                    .build();

            articles.add(article);

        }



        NewsDTO newsDto = NewsDTO.builder()
                .article(articles)
                .build();

        return newsDto;
    }
}

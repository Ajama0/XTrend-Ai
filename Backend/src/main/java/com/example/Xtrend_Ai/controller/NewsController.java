package com.example.Xtrend_Ai.controller;


import com.example.Xtrend_Ai.dto.NewsRequest;
import com.example.Xtrend_Ai.dto.NewsResponse;
import com.example.Xtrend_Ai.entity.News;
import com.example.Xtrend_Ai.service.NewsApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("api/v1/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsApiService newsApiService;
    @GetMapping(path="/")
    public void persistTrendingNews() {
        /**
         * the news request object is what we pass in to make the call to the api
         * we can define query parameters that we need. null parameters are not considered
         *
         */
        NewsRequest newsRequest = NewsRequest.builder()
                .language("en")
                .country("us")
                .build();

        newsApiService.getTopHeadlines(newsRequest,
                new NewsApiService.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(NewsResponse newsResponse) {
                        /// handles how we save the articles
                        newsApiService.saveArticles(newsResponse);
                    }

                    @Override
                    public void onFailure(String error) {
                        log.error(error);

                    }
                }
        );

    }

    @GetMapping(path="all/articles")
    public ResponseEntity<List<News>> getAllTrendingNews() {
        List<News> trending = newsApiService.findAllNews();
        return ResponseEntity.ok(trending);

    }
}

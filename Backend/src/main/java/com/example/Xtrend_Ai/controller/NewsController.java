package com.example.Xtrend_Ai.controller;


import com.example.Xtrend_Ai.dto.NewsRequest;
import com.example.Xtrend_Ai.dto.NewsResponse;
import com.example.Xtrend_Ai.entity.News;
import com.example.Xtrend_Ai.service.NewsApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/news")
@RequiredArgsConstructor
public class NewsController {

    private NewsApiService newsApiService;
    @GetMapping(path="/trending")
    public ResponseEntity<List<News>> getTrendingNews() {
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
                        throw new RuntimeException(error);

                    }
                }
        );
        return ResponseEntity.ok(newsApiService.findAllNews());
    }
}

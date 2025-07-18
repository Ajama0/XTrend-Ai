package com.example.Xtrend_Ai.controller;


import com.example.Xtrend_Ai.dto.ArticleResponse;
import com.example.Xtrend_Ai.dto.NewsRequest;
import com.example.Xtrend_Ai.dto.NewsResponse;
import com.example.Xtrend_Ai.entity.News;
import com.example.Xtrend_Ai.service.DiffBotService;
import com.example.Xtrend_Ai.service.NewsApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
@Slf4j
@RestController
@EnableScheduling
@RequestMapping("api/v1/news")
public class NewsController {

    private final NewsApiService newsApiService;
    public NewsController(NewsApiService newsApiService, DiffBotService diffBotService) {
        this.newsApiService = newsApiService;
    }


    @GetMapping(path="/trending")
    public ResponseEntity<NewsResponse> getTrendingNews() throws IOException {
        NewsResponse newsResponse = newsApiService.getNews();
        return new ResponseEntity<>(newsResponse, HttpStatus.OK);

    }


//    @GetMapping(path="/")
//    public ResponseEntity<List<NewsResponse>> persistTrendingNews() {
//        /**
//         *
//         * we can define query parameters that we need when making the call to the news api
//         */
//        List<NewsResponse> response = newsApiService.getTopHeadlines();
//        return new ResponseEntity<>(response, HttpStatus.OK);
//
//
//    }


        /**
         * This allows us to make the call to the DB once the articles have been persisted.
         * @return
         */
        @GetMapping(path = "all/articles")
        public ResponseEntity<List<News>> getAllTrendingNews () {
            List<News> trending = newsApiService.findAllNews();
            return ResponseEntity.ok(trending);

        }


    }








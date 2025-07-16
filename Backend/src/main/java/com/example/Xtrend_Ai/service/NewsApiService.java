package com.example.Xtrend_Ai.service;
import com.example.Xtrend_Ai.client.NewsApi.ApiClient;
import com.example.Xtrend_Ai.dto.NewsRequest;
import com.example.Xtrend_Ai.dto.NewsResponse;
import com.example.Xtrend_Ai.entity.News;
import com.example.Xtrend_Ai.exceptions.ArticleNotFoundException;
import com.example.Xtrend_Ai.repository.NewsRepository;
import com.example.Xtrend_Ai.utils.Article;
import com.example.Xtrend_Ai.utils.NewsUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class NewsApiService {


    @Value("${news.api}")
    private String newsApiKey;

    private final NewsRepository newsRepository;
    private final ApiClient apiClient;
    private final ObjectMapper objectMapper;


    public NewsResponse getNews() throws IOException {
        NewsRequest newsRequest = NewsRequest
                .builder()
                .country(List.of("gb,us,ca"))
                .Language("en")
                .category(List.of("politics", "sports", "business", "entertainment","technology"))
                .image("1")
                .priorityDomain("medium")
                .removeDuplicate("1")
                .build();

        /// convert lists into strings
        String category = String.join(",", newsRequest.getCategory());
        String country = String.join(",", newsRequest.getCountry());

        HttpUrl baseUrl = HttpUrl.parse("https://newsdata.io/api/1/latest");
        HttpUrl.Builder builder = baseUrl.newBuilder().addQueryParameter("apikey", newsApiKey)
                .addQueryParameter("country", country)
                .addQueryParameter("language", newsRequest.getLanguage())
                .addQueryParameter("category",category)
                .addQueryParameter("image", newsRequest.getImage())
                .addQueryParameter("removeduplicate", newsRequest.getRemoveDuplicate())
                .addQueryParameter("prioritydomain", newsRequest.getPriorityDomain());

        HttpUrl url = builder.build();


        ResponseBody responseBody = apiClient.fetchTopStories(url);
        String body = responseBody.string();
        log.info("NewsApiService getNews body: {}", body);
        NewsResponse newsResponse = objectMapper.readValue(body, NewsResponse.class);

        return newsResponse;

    }



    @Scheduled(fixedDelay = 1000 * 60 * 60)
    @Async
    public void defineBase() throws IOException {
        log.info("running now");

        HttpUrl BaseUrl = HttpUrl.parse("https://newsdata.io/api/1/latest");
        GetLatestNews(BaseUrl, null, 1);

    }




    //TODO recusrive function

    public void GetLatestNews(HttpUrl baseUrl, String nextPage, int requestCount) throws IOException {
        log.info("inside recursive function");




        /// here we use the api client to return an impl of the base ApiService.
        NewsRequest newsRequest = NewsRequest
                .builder()
                .country(List.of("gb","us","ca"))
                .Language("en")
                .category(List.of("politics", "sports", "business", "entertainment","technology"))
                .image("1")
                .priorityDomain("medium")
                .removeDuplicate("1")
                .build();

        /// convert lists into strings
        String category = String.join(",", newsRequest.getCategory());
        String country = String.join(",", newsRequest.getCountry());


        HttpUrl.Builder builder = baseUrl.newBuilder().addQueryParameter("apikey", newsApiKey)
                .addQueryParameter("country", country)
                .addQueryParameter("language", newsRequest.getLanguage())
                .addQueryParameter("category",category)
                .addQueryParameter("image", newsRequest.getImage())
                .addQueryParameter("removeduplicate", newsRequest.getRemoveDuplicate())
                .addQueryParameter("prioritydomain", newsRequest.getPriorityDomain());


        /// the intiial call we will set nextPage to null
        if (nextPage != null && !nextPage.isEmpty()) {
            builder.addQueryParameter("page", nextPage);
        }


        HttpUrl url = builder.build();
        /// we can make the request, handle the processing of response and also fetch the next page string
        ResponseBody responseBody = apiClient.fetchTopStories(url);
        String body = responseBody.string();
        log.error("body {}", body);
        NewsResponse newsResponse = objectMapper.readValue(body, NewsResponse.class);
        log.info("newsResponse after deserializing{}", newsResponse.toString());
        requestCount++;
        log.info("requests count {}", requestCount);
        saveNews(newsResponse);


        /// from the news response we can extract the next page token and recall the function
        nextPage = newsResponse.getNextPage();
        log.info("nextPage ISSSSSSSSS {}", nextPage);



        /// recursively call this function with the next page and rebuild the builder object
        /// we can validate the request count before calling it again
        if(requestCount >=3){
            return;
        }

        GetLatestNews(baseUrl,nextPage, requestCount);

    }





    public void saveNews(NewsResponse newsResponse) {
    if (newsResponse == null || newsResponse.getResults().isEmpty()) {
        throw new ArticleNotFoundException("no Articles were  found");
    }

    for (Article article : newsResponse.getResults()) {
        News news = News.builder()
                .article(article)
                .build();
        newsRepository.save(news);
    }
}




    /**
     * allows us to fetch all the persisted news
     *
     * @return a list of news objects
    */
    public List<News> findAllNews() {
        List<News> newsList = newsRepository.findAll();
        if (newsList.isEmpty()) {
        throw new ArticleNotFoundException("no Articles were found");
    }
    return newsList;

}








}

package com.example.Xtrend_Ai.service;
import com.example.Xtrend_Ai.client.DiffBot.DiffBotClient;
import com.example.Xtrend_Ai.client.NewsApi.ApiClient;
import com.example.Xtrend_Ai.client.NewsApi.ApiService;
import com.example.Xtrend_Ai.dto.DiffBotArticle;
import com.example.Xtrend_Ai.dto.DiffBotResponse;
import com.example.Xtrend_Ai.dto.NewsRequest;
import com.example.Xtrend_Ai.dto.NewsResponse;
import com.example.Xtrend_Ai.entity.Article;
import com.example.Xtrend_Ai.entity.News;
import com.example.Xtrend_Ai.exceptions.ArticleNotFoundException;
import com.example.Xtrend_Ai.repository.NewsRepository;
import com.example.Xtrend_Ai.utils.NewsUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Callback;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class NewsApiService {


    @Value("${news.api}")
    private String newsApiKey;

    private final NewsRepository newsRepository;
    private final ApiClient apiClient;
    private final DiffBotClient diffBotClient;

    public interface ArticlesResponseCallback {
        void onSuccess(NewsResponse newsResponse);

        void onFailure(String error);
    }

    public void getTopHeadlines(NewsRequest newsRequest, final ArticlesResponseCallback callback) {
        /// here we use the api client to return an impl of the base ApiService.


        log.info("api key is....{}", newsApiKey);
        Map<String, String> query = NewsUtils.generateQuery(newsApiKey);

        log.info("api key inside query is : {}", query.get("apiKey"));
        query.put("country", newsRequest.getCountry());
        query.put("language", newsRequest.getLanguage());
        query.put("category", newsRequest.getCategory());
        query.put("sources", newsRequest.getSources());
        query.put("q", newsRequest.getQ());
        query.put("pageSize", newsRequest.getPageSize());
        query.put("page", newsRequest.getPage());

        query.values().removeAll(Collections.singleton(null));
        query.values().removeAll(Collections.singleton("null"));

        /// returns the service class that has the impl of the get request
        ApiService apiService = apiClient.getApiService();
        apiService.topHeadlines(query)
                .enqueue(new Callback<NewsResponse>() {
                    @Override
                    public void onResponse(Call<NewsResponse> call, retrofit2.Response<NewsResponse> response) {
                        if (response.code() == HttpURLConnection.HTTP_OK) {
                            log.info("ok some data returned");
                            callback.onSuccess(response.body());
                            log.info("top headlines success : {}", response.body());
                        } else {
                            try {
                                assert response.errorBody() != null;
                                callback.onFailure(response.errorBody().string());
                            } catch (IOException e) {
                                log.error(e.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<NewsResponse> call, Throwable throwable) {
                        callback.onFailure(throwable.getMessage());
                    }
                });

        log.info("synchronous process completed");

    }


    /**
     * here we will handle how the OnSuccess function is handled
     */

    @Transactional
    public void saveNews(NewsResponse newsResponse) {
        if (newsResponse == null || newsResponse.getArticles().isEmpty()) {
            throw new ArticleNotFoundException("no Articles were  found");
        }

        for (Article article : newsResponse.getArticles()) {
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


    /**
     * @param id  - the id of the article the user wants to generate a blog from
     * @param url - url pointing to the exact article which allows us to extract content
     * @return text - represents extracted textual content from the url
     */
    public String generateBlog(Long id, String url) {
        Optional<News> news = newsRepository.findById(id);
        if (news.isEmpty()) {
            throw new ArticleNotFoundException("no Articles were found");
        }
        News retrieveNews = news.get();

        if (!retrieveNews.getArticle().getUrl().equals(url)) {
            throw new RuntimeException("urls do not match");
        }


        /// we need to pass the token and the url as query params to the api
        String endpoint = "https://api.diffbot.com/v3/article?token=%s&url=%s".
                formatted(diffBotClient.getApiKey(), url);

        /// handle the returned response from diffbot (already extracted the body)
        DiffBotResponse diffbotresponse;
        try (ResponseBody responseBody = diffBotClient.diffBotRequest(endpoint)) {
            log.info("diffbot response : {}", responseBody.string());
            ObjectMapper objectMapper = new ObjectMapper();
            diffbotresponse = objectMapper.readValue(responseBody.string(), DiffBotResponse.class);
            log.info("converting th object to our pojo : {}", diffbotresponse.toString());


            if (diffbotresponse.getObjects() != null && !diffbotresponse.getObjects().isEmpty()) {
                DiffBotArticle article = diffbotresponse.getObjects().get(0);
                /// only returns the text from the returned json
                return article.getText();
            } else {
                throw new RuntimeException("response is either empty or null");
            }

        } catch (Exception e) {
            throw new RuntimeException("error occurred while requesting diff bot");
        }


    }





}

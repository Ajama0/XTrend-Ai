package com.example.Xtrend_Ai.service;
import com.example.Xtrend_Ai.client.ApiClient;
import com.example.Xtrend_Ai.client.ApiService;
import com.example.Xtrend_Ai.dto.NewsRequest;
import com.example.Xtrend_Ai.dto.NewsResponse;
import com.example.Xtrend_Ai.entity.Article;
import com.example.Xtrend_Ai.entity.News;
import com.example.Xtrend_Ai.exceptions.ArticleNotFoundException;
import com.example.Xtrend_Ai.repository.NewsRepository;
import com.example.Xtrend_Ai.utils.NewsUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import retrofit2.Call;
import retrofit2.Callback;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class NewsApiService {


    @Value("${news.api}")
    private String newsApi;
    private final NewsRepository newsRepository;
    ApiClient apiClient = new ApiClient();
    ApiService apiService = apiClient.getApiService();




    public interface ArticlesResponseCallback {
        void onSuccess(NewsResponse newsResponse);
        void onFailure(String error);
    }

    public void getTopHeadlines(NewsRequest newsRequest, final ArticlesResponseCallback callback){
        /// here we use the api client to return an impl of the base ApiService.

        Map<String, String> query = NewsUtils.generateQuery(newsApi);
        query.put("country", newsRequest.getCountry());
        query.put("language", newsRequest.getLanguage());
        query.put("category", newsRequest.getCategory());
        query.put("sources", newsRequest.getSources());
        query.put("q", newsRequest.getQ());
        query.put("pageSize", newsRequest.getPageSize());
        query.put("page", newsRequest.getPage());

        query.values().removeAll(Collections.singleton(null));
        query.values().removeAll(Collections.singleton("null"));


        apiService.topHeadlines(query)
                .enqueue(new Callback<NewsResponse>() {
                    @Override
                    public void onResponse(Call<NewsResponse> call, retrofit2.Response<NewsResponse> response) {
                        if (response.code() == HttpURLConnection.HTTP_OK){
                            callback.onSuccess(response.body());
                        }

                        else{
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
    }


    /**
     * here we will handle how the OnSuccess function is handled
     */

    public void saveArticles(NewsResponse newsResponse){
        if(newsResponse == null || newsResponse.getArticles().isEmpty()){
            throw new ArticleNotFoundException("no Articles were  found");
        }

        News news = News.builder()
                .articles(newsResponse.getArticles())
                .build();

        newsRepository.save(news);
    }


    /**
     * allows us to fetch all the persisted news
     * @return a list of news objects
     */
    public List<News> findAllNews(){
        List<News> newsList = newsRepository.findAll();
        if(newsList.isEmpty()){
            throw new ArticleNotFoundException("no Articles were found");
        }
        return newsList;

    }

}

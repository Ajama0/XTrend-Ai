package com.example.Xtrend_Ai.service;



import com.example.Xtrend_Ai.client.ApiClient;
import com.example.Xtrend_Ai.client.ApiService;
import com.example.Xtrend_Ai.dto.NewsRequest;
import com.example.Xtrend_Ai.dto.NewsResponse;
import com.example.Xtrend_Ai.repository.NewsRepository;
import com.example.Xtrend_Ai.utils.NewsUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import retrofit2.Call;

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




    public void getTopHeadlines(NewsRequest newsRequest, final ArticlesResponseCallback callback){
        /// here we use the api client to return an impl of the base ApiService.
        ApiClient apiClient = new ApiClient();
        ApiService apiService = apiClient.getApiService();

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
                .enqueue(new Callback<ArticleResponse>() {
                    @Override
                    public void onResponse(Call<ArticleResponse> call, retrofit2.Response<ArticleResponse> response) {
                        if (response.code() == HttpURLConnection.HTTP_OK){
                            callback.onSuccess(response.body());
                        }

                        else{
                            try {
                                callback.onFailure(errMsg(response.errorBody().string()));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ArticleResponse> call, Throwable throwable) {
                        callback.onFailure(throwable);
                    }
                });
    }



}

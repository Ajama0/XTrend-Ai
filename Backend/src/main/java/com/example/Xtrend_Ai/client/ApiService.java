package com.example.Xtrend_Ai.client;

import com.example.Xtrend_Ai.dto.NewsResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

import java.util.List;
import java.util.Map;


public interface ApiService {


    /**
     * retrofit provides us with the implementation of the http request using a proxy
     * @param query - the set of queries that we'll use to pass alongside the request
     * @return - we return a C
     */

    @GET(value = "/v2/top-headlines")
    Call<List<NewsResponse>> topHeadlines(@QueryMap Map<String,String> query);
}

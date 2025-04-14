package com.example.Xtrend_Ai.client.NewsApi;

import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;


@Component
public class ApiClient {


    private final OkHttpClient httpClient;

    /**
     * spring now knows which instance of Okhttpclient to inject based on their configs
     * @param httpClient - the configured http client with timeouts
     */
    public ApiClient(@Qualifier("NewsApiHttpClient") OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }
    public Retrofit getRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://newsapi.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.newBuilder().build())
                .build();

        return retrofit;
    }


    /**
     * retrofit defines the implementation for the apiservice class
     * @return an apiservice object with impl ready to call enqueue to run the http call
     */
    public ApiService getApiService() {
        return getRetrofit().create(ApiService.class);
    }


}

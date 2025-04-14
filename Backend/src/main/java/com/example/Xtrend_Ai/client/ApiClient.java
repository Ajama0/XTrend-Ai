package com.example.Xtrend_Ai.client;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class ApiClient {

    OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

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

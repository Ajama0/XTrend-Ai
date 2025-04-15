package com.example.Xtrend_Ai.config;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class HttpClientConfig {


    @Bean
    public OkHttpClient DifBotHttpClient() {
        return new OkHttpClient();
    }

    @Bean
    public OkHttpClient NewsApiHttpClient() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        return httpClient;

    }


}

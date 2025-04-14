package com.example.Xtrend_Ai.client.DiffBot;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.io.IOException;

@Slf4j
@Configuration
public class DiffBotClient {

    @Value("${diffbot.api.key}")
    private String DIFF_API_KEY;

    /**
     * difbot allows us to extract the textual content from the article URL, whenever a user is set to generate a blog
     * or their own article.
     */

    /**
     * whenever we inject this dependency, the request will already be configured, hence we can pass this request to the client
     * @param url - url that is going to be scraped
     * @return - response object
     */


    public Request diffBotRequest(String url){;
        Request request = new Request.Builder()
                .url() /// the url will be specific article the user request plus it will include our token
                .get()
                .addHeader("accept", "application/json")
                .build();

                return request;

    }







}

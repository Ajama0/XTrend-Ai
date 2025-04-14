package com.example.Xtrend_Ai.client.DiffBot;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClient;

import java.io.IOException;

@Slf4j
public class DiffBotClient {

    @Value("${diffbot.api.key}")
    private String DIFF_API_KEY;

    /**
     * difbot allows us to extract the textual content from the article URL, whenever a user is set to generate a blog
     * or their own article.
     */



    public Response getDiffBotArticle(String url)  {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url) /// the url will be specific article the user request plus it will include our token
                .get()
                .addHeader("accept", "application/json")
                .build();

            try {
                Response response = client.newCall(request).execute();
                return response;
            }catch(IOException e) {
                log.error(e.getMessage());

            }
    }



}

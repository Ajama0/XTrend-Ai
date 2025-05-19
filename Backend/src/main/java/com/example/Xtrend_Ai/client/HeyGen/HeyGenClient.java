package com.example.Xtrend_Ai.client.HeyGen;

import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;



@Component
public class HeyGenClient {
    @Value("${heygen.api}")
    private String apikey;

    private OkHttpClient client;

    public HeyGenClient(@Qualifier("HeyGen")OkHttpClient okHttpClient) {
        this.client = okHttpClient;
    }


    public Response ListOfAvatars(String url) {
        Request request = new Request.Builder()
                .get()


    }


    public Response ListOfVoices(String url) {

    }


    public Response GenerateVideo(String url){

    }

}

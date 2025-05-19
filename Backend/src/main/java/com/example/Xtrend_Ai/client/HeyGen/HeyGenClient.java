package com.example.Xtrend_Ai.client.HeyGen;

import com.example.Xtrend_Ai.dto.HeyGenRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class HeyGenClient {
    @Value("${heygen.api}")
    private String apikey;

    private OkHttpClient client;

    public HeyGenClient(@Qualifier("HeyGen")OkHttpClient okHttpClient) {
        this.client = okHttpClient;
    }


    public Response ListOfAvatars(String url) {
        return null;


    }


    public Response ListOfVoices(String url) {
    return null;
    }
    public Response GenerateVideo(HeyGenRequest heyGenRequest) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(heyGenRequest);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Request request = new Request.Builder()
                .url("https://api.heygen.com/v2/video/generate")
                .post(requestBody)
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/json")
                .addHeader("x-api-key", apikey)
                .build();

        Response response = client.newCall(request).execute();
        return response;
    }

}

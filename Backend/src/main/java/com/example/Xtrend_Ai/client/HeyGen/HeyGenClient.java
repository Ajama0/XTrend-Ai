package com.example.Xtrend_Ai.client.HeyGen;

import com.example.Xtrend_Ai.dto.HeyGenRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@Slf4j
public class HeyGenClient {
    @Value("${heygen.api}")
    private String apikey;

    private OkHttpClient client;

    public HeyGenClient(@Qualifier("HeyGen")OkHttpClient okHttpClient) {
        this.client = okHttpClient;
    }


    /***
     *
     * @return list of avatars in which users can choose from
     * @throws IOException
     */
    public Response ListOfAvatars()  throws IOException {

        Request request = new Request.Builder()
                .url("https://api.heygen.com/v2/avatars")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("x-api-key", apikey)
                .build();

        Response response = client.newCall(request).execute();
        assert response.body() != null;
        return response;


    }

    /**
     * list of voices in which users can choose from
     * @return Response which contains the Json data
     */
    public Response ListOfVoices() throws IOException {
        Request request = new Request.Builder()
                .url("https://api.heygen.com/v2/voices")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("x-api-key", apikey)
                .build();

        Response response = client.newCall(request).execute();
        return response;
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

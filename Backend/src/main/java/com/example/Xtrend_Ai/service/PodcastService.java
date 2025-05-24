package com.example.Xtrend_Ai.service;


import com.example.Xtrend_Ai.dto.PodcastRequest;
import com.example.Xtrend_Ai.dto.PodcastResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

@Service

public class PodcastService {

    private final OkHttpClient client;

    public PodcastService(@Qualifier("Podcast")OkHttpClient client) {
        this.client = client;
    }


    public PodcastResponse getPodcast(PodcastRequest podcastRequest) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(podcastRequest);

            RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
            Request request = new Request.Builder()
                    .url("http://localhost:5000/generate-podcast") // Flask running locally
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();

            if (response.isSuccessful() && response.body() != null) {
                String responseJson = response.body().string();
                return mapper.readValue(responseJson, PodcastResponse.class);
            } else {
                throw new RuntimeException("Failed to get podcast from Flask: " + response.message());
            }

        } catch (Exception e) {
            throw new RuntimeException("Error calling Flask service", e);
        }
    }





    }



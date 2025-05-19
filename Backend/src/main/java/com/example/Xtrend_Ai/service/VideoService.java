package com.example.Xtrend_Ai.service;

import com.example.Xtrend_Ai.client.HeyGen.HeyGenClient;
import com.example.Xtrend_Ai.dto.AvatarResponse;
import com.example.Xtrend_Ai.dto.HeyGenRequest;
import com.example.Xtrend_Ai.dto.HeyGenResponse;
import com.example.Xtrend_Ai.dto.VoiceResponse;
import com.example.Xtrend_Ai.repository.NewsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpClient;

@Service
@RequiredArgsConstructor
public class VideoService {
    private final HeyGenClient heyGenClient;
    private final ObjectMapper objectMapper;
    private final NewsRepository newsRepository;
    private final DiffBotService diffBotService;
    private final GPTService gptService;


    public AvatarResponse fetchAvatars() {
        try {
            Response response = heyGenClient.ListOfAvatars();
            if (response.code() == 200) {
                assert response.body() != null;
                AvatarResponse avatarResponse = objectMapper.readValue(response.body().toString(), AvatarResponse.class);
                return avatarResponse;
            }else{
                throw new RuntimeException("a status code other than 200 returned" + response.code());
            }
        }catch (IOException e) {
            throw new RuntimeException("error occurred processing avatar response", e);
        }

    }

    public VoiceResponse fetchVoices() {
        try {
            Response response = heyGenClient.ListOfAvatars();
            if (response.code() == 200) {
                assert response.body() != null;
                VoiceResponse voiceResponse = objectMapper.readValue(response.body().toString(), VoiceResponse.class);
                return voiceResponse;
            }else{
                throw new RuntimeException("a status code other than 200 returned" + response.code());
            }
        }catch (IOException e) {
            throw new RuntimeException("error occurred processing avatar response", e);
        }

    }

    public HeyGenResponse GenerateVideo(Long id, String username) {
        /***
         * the workflow of generating a video consists of passing in the id of the trending news and the user to generate content
         * after validating the news object we pass the url to the DiffBot API which uses ML to scrape article content
         * the content is fed into our LLM to generate a video script
         * this video script is then passed into HeyGen's input text property.
         */



        }

    }

}

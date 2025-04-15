package com.example.Xtrend_Ai.service;

import com.example.Xtrend_Ai.dto.GPTRequest;
import com.example.Xtrend_Ai.dto.GptResponse;
import com.example.Xtrend_Ai.utils.ChatMessage;
import com.example.Xtrend_Ai.utils.Choices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class GPTService {

    private final RestClient restClient;

    @Value("${gpt.key}")
    private String gptKey;

    @Value("${gpt.url}")
    private String gptUrl;


    public String getGptResponse(String content){

        /// build the message object
        ChatMessage chatMessage = ChatMessage
                .builder()
                .role("user")
                .content(prompt)
                .build();

        /// building the request object
        GPTRequest request = GPTRequest.builder()
                .model("gpt-4o")
                .messages(Collections.singletonList(chatMessage))
                .build();

        ResponseEntity<GptResponse> response = restClient.post()
                .uri(gptUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .headers(header ->header.setBearerAuth(gptKey) )
                .retrieve()
                .toEntity(GptResponse.class);

        if(response.getStatusCode().is2xxSuccessful() && !response.getBody().getChoices().isEmpty()){
            log.info("success");
            Choices returnedChoice = response.getBody().getChoices().get(0);
            String messageResponse = returnedChoice.getMessages().getContent();
            return messageResponse;

        }
    }

    public String generatePrompt(String content){
        return
                "given this article, read it in depth thoroughly. I will then request you generate a blog from this article
                the blog should always remain within the context of the article but it is to



                "
    }

}

package com.example.Xtrend_Ai.service;

import com.example.Xtrend_Ai.client.DiffBot.DiffBotClient;
import com.example.Xtrend_Ai.utils.DiffBotArticle;
import com.example.Xtrend_Ai.dto.DiffBotResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DiffBotService {

    private final DiffBotClient diffBotClient;

    public String extractTextFromArticle(String url) {
        /// we need to pass the token and the url as query params to the api
        String endpoint = "https://api.diffbot.com/v3/article?token=%s&url=%s".
                formatted(diffBotClient.getApiKey(), url);

        /// handle the returned response from diffbot (already extracted the body)
        DiffBotResponse diffbotresponse;
        try (
                ResponseBody responseBody = diffBotClient.diffBotRequest(endpoint)) {
            log.info("successfully returned the data");
            ObjectMapper objectMapper = new ObjectMapper();
            diffbotresponse = objectMapper.readValue(responseBody.string(), DiffBotResponse.class);
            log.info("successfully deserialized the data");


            if (diffbotresponse.getObjects() != null && !diffbotresponse.getObjects().isEmpty()) {
                DiffBotArticle article = diffbotresponse.getObjects().get(0);
                /// only returns the text from the returned json
                return article.getText();
            } else {
                throw new RuntimeException("response is either empty or null");
            }

        } catch (Exception e) {
            throw new RuntimeException("error occurred while requesting diff bot");
        }
    }
}

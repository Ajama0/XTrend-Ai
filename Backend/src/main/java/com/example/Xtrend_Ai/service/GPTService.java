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

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class GPTService {

    private final RestClient restClient;

    @Value("${gpt.key}")
    private String gptKey;

    @Value("${gpt.url}")
    private String gptUrl;


    public String getGptResponse(String article, String username, Boolean videoScript){
        String developerPrompt;
        /// if its a video script prompt we use this function else if its a blog generation we use blog prompt
        if(videoScript){
            log.info("generating video script");
            developerPrompt = generateVideoScriptPrompt();
        }else {
             developerPrompt = generateBlogPrompt(username);
        }

        /// this is the developer instruction prioritized ahead of the user prompt
        ChatMessage developer = ChatMessage
                .builder()
                .role("system")
                .content(developerPrompt)
                .build();

        /// llm uses the developer instruction to handle this prompt
        ChatMessage user = ChatMessage
                .builder()
                .role("user")
                .content(article)
                .build();

        /// building the request object
        GPTRequest request = GPTRequest.builder()
                .model("gpt-4o")
                .messages(Arrays.asList(developer, user))
                .temperature(0.5)
                .max_tokens(700)
                .build();

        log.info("before making request");
        ResponseEntity<GptResponse> response = restClient.post()
                .uri(gptUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .headers(header ->header.setBearerAuth(gptKey) )
                .retrieve()
                .toEntity(GptResponse.class);

        log.info("after making response");
        log.info("status code is{}", response.getStatusCode());
        if(response.getStatusCode().is2xxSuccessful() && !Objects.requireNonNull(response.getBody()).getChoices().isEmpty()){
            log.info("success");
            Choices returnedChoice = response.getBody().getChoices().get(0);
            return returnedChoice.getMessage().getContent();

        }else{
            log.info("status code :{}", response.getStatusCode());
            throw new RuntimeException("error has occurred");

        }
    }

    public String generateBlogPrompt(String username){
        return """
           Read the following article thoroughly.
            You are an expert blog writer. Based on the content of the article,
            generate a detailed and engaging blog post while strictly staying within the article's topic.
           Follow these guidelines:
           Do not echo the instructions back. Only output the final narration script.

           1. Content Adaptation:
              - Adjust the tone to be conversational and relatable for a blog audience.
              - Rewrite and restructure the original text to produce a fresh, original piece—avoid verbatim copying.
              - Ensure that the blog post reflects the key points and insights of the article.

           2. Length Considerations:
              - Aim for a blog post of approximately 1000 words.
              - Condense or expand the information as needed while preserving clarity and substance.

          
            - **Do not** use markdown, bullet lists, asterisks, or numbering—write in flowing paragraphs with clear plain-text subheadings. 
            - **Structure** your post as follows:
            
            3. A **catchy headline** that includes the article’s key topic or phrase.
            4. An **engaging introduction** (1–2 paragraphs) that hooks the reader. 
            5. **Three body sections** (2–3 paragraphs each), each under a descriptive subheading, covering:
               - The **human angle** or anecdote from the article 
               - The **core facts** or context (data, timeline, actors) 
               - The **broader implications** or “so what” for readers 
            6. A **concise conclusion** (1 paragraph) that summarizes the main takeaways and ends with a call to action or thought-provoking question.
              
           7. Attribution:
              - At the very end of the blog post, include an attribution line that acknowledges the creator. Format it as:
                "Blog generated by: %s". only include this in the end

           8. Consistency:
              - The entire blog must remain on topic
              - Do not introduce new topics or unrelated information.

           The article will be provided to you.
          
           """.formatted(username);
    }


    public String generateVideoScriptPrompt(){

        /// instructions we give our LLM to generate a video script based on the trending news.
        return """
                You are a professional broadcast‐news scriptwriter creating a narration script for a presenter.
                Given a factual news article, produce a clear, engaging, and human-sounding video script—word-for-word—for a clip up to 3 minutes long (roughly 450 words max).
                
                Do not echo the instructions back. Only output the final narration script.
                
                Your script must:
                1. Open with a strong, attention-grabbing hook in 1–2 sentences.
                2. Accurately relay the key facts, data, and quotes in logical order.
                3. Use smooth, spoken-style transitions (“Next…,” “Meanwhile…,” etc.).
                4. Maintain an energetic yet authoritative tone.
                5. You may include a brief personal insight or perspective, so long as it’s directly tied to the topic and factually sound (no unfounded opinions).
                6. Stay strictly on topic—do not introduce outside information.
                7. Write complete, conversational sentences (no bullet lists).
                8. Close with a concise wrap-up line reinforcing the main takeaway.
                
                The article will be provided to you.
                """;
    }

}

package com.example.Xtrend_Ai.client.OpenAi;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@Configuration
@Data
public class GPTClient {


    @Value("${gpt.key}")
     private String GptKey;

    RestClient restClient = RestClient.create();

}

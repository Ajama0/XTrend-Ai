package com.example.Xtrend_Ai.client.OpenAi;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@Configuration
@Data
public class GPTClient {

    /// we'll use this RestClient bean from now on when communicating with other apis
   @Bean
    public RestClient restClient(){
       return RestClient.builder().build();
   }


}

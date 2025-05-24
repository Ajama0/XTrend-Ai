package com.example.Xtrend_Ai.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CORS implements WebMvcConfigurer {


    /***
     *
     * @param registry - defines the cors configuration, what methods and which origins are allowed to send requests
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(String.valueOf(Arrays.asList("http://localhost:49220/", "http://localhost:8000/")))
                .allowCredentials(true)
                .allowedHeaders("*");
    }
}

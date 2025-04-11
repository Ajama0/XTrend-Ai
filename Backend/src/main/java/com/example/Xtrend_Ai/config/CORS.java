package com.example.Xtrend_Ai.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CORS implements WebMvcConfigurer {


    /***
     *
     * @param registry - defines the cors configuration, what methods and which origins are allowed to send requests
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(" http://localhost:49111/")
                .allowCredentials(true)
                .allowedHeaders("*");
    }
}

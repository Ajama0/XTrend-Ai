package com.example.Xtrend_Ai.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;

@Configuration
public class cacheConfig {


    @Bean
    public Cache<userPodcastIds, signedUrl> cache(){
        return Caffeine.newBuilder()
                        .expireAfterWrite(Duration.ofMinutes(2))
                                .build();
    }

    public record userPodcastIds(Long userId, Long podcastId) {}

    public record signedUrl(URL url){}

}

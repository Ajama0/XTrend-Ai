package com.example.Xtrend_Ai.service;


import com.example.Xtrend_Ai.dto.PodcastRequest;
import com.example.Xtrend_Ai.dto.PodcastResponse;
import com.example.Xtrend_Ai.entity.News;
import com.example.Xtrend_Ai.entity.Podcast;
import com.example.Xtrend_Ai.entity.User;
import com.example.Xtrend_Ai.enums.Status;
import com.example.Xtrend_Ai.exceptions.ArticleNotFoundException;
import com.example.Xtrend_Ai.exceptions.UsernameNotFoundException;
import com.example.Xtrend_Ai.repository.NewsRepository;
import com.example.Xtrend_Ai.repository.PodcastRepository;
import com.example.Xtrend_Ai.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.ResponseBytes;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PodcastService {

    private final WebClient client = WebClient.create("http://localhost:5000");
    private final PodcastRepository podcastRepository;
    private final NewsRepository newsRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;



    public PodcastResponse getPodcast(PodcastRequest podcastRequest)  {

            News news  = newsRepository.findById(podcastRequest.getNewsId()).orElseThrow(()->new
                    ArticleNotFoundException("News with id " + podcastRequest.getNewsId() + " not found"));

            User user  = userRepository.findByUsername(podcastRequest.getUsername()).orElseThrow(()->
                    new UsernameNotFoundException("User not found"));

            String key = UUID.randomUUID().toString();

            Podcast podcast = Podcast.builder()
                    .key(key)
                    .date(LocalDateTime.now().toString())
                    .news(news)
                    .status(Status.PROCESSING)
                    .user(user)
                    .build();

            podcastRepository.save(podcast);

            try {
                String JSON = objectMapper.writeValueAsString(podcastRequest);
                podcastRequest.setArticleUrl(news.getArticle().getSource_url());
                RequestBody body = RequestBody.create(MediaType.parse("application/json"), JSON);

                /// Setting up request
                Mono<Byte[]> response = client.get()
                        .uri("/generate-podcast")
                        .retrieve()
                        .bodyToMono(Byte[].class);

                /// accessing the raw audio
                Byte[] audio = response.block();

                if (audio != null && audio.length > 0) {
                    uploadPodcast("", podcast.getId(), audio);

                } else {
                    throw new RuntimeException("audio is empty or null ");
                }

            }catch(IOException e){
                throw new RuntimeException("Failed to get podcast from Flask: " + e.getMessage());

                }


        return PodcastResponse.builder()
                .podcastId(podcast.getId())
                .status(podcast.getStatus())
                .build();
    }




    public void uploadPodcast(String bucket,Long podcastId, Byte[] bytes ) {}





    }



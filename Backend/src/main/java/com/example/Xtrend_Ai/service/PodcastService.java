package com.example.Xtrend_Ai.service;


import com.example.Xtrend_Ai.Aws.S3Service;
import com.example.Xtrend_Ai.dto.PodcastRequest;
import com.example.Xtrend_Ai.dto.PodcastResponse;
import com.example.Xtrend_Ai.entity.News;
import com.example.Xtrend_Ai.entity.Podcast;
import com.example.Xtrend_Ai.entity.User;
import com.example.Xtrend_Ai.enums.Status;
import com.example.Xtrend_Ai.exceptions.ArticleNotFoundException;
import com.example.Xtrend_Ai.exceptions.AudioNotFoundException;
import com.example.Xtrend_Ai.exceptions.UsernameNotFoundException;
import com.example.Xtrend_Ai.repository.NewsRepository;
import com.example.Xtrend_Ai.repository.PodcastRepository;
import com.example.Xtrend_Ai.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
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
    private final S3Service s3Service;


    @Value("${bucket.name}")
    private String bucketName;

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
                podcastRequest.setArticleUrl(news.getArticle().getSource_url());

                client.post()
                        .uri("/generate-podcast")
                        .bodyValue(podcastRequest)
                        .retrieve()
                        .bodyToMono(byte[].class)
                        .subscribe(bytes -> uploadPodcast(bucketName, podcast.getId(), bytes));


            }catch(RuntimeException e){
                throw new RuntimeException("Failed to get podcast from Flask: " + e.getMessage());

                }

            /// returned straight away for client side polling.
        return PodcastResponse.builder()
                .podcastId(podcast.getId())
                .status(podcast.getStatus())
                .build();
    }




    public void uploadPodcast(String bucket,Long podcastId, byte[] bytes )  {
        if(bytes == null || bytes.length == 0) {
            throw new AudioNotFoundException("audio either null or empty");
        }

        Podcast podcast = podcastRepository.findById(podcastId).orElseThrow(()->
                new AudioNotFoundException("podcast with id " + podcastId + " not found"));


        String key = "podcasts/upload/%s/%s".formatted(podcastId, podcast.getKey());
        s3Service.putObject(bucket, key, bytes);
        podcast.setStatus(Status.COMPLETED);

    }












    }



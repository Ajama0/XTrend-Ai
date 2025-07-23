package com.example.Xtrend_Ai.service;


import com.example.Xtrend_Ai.Aws.S3Service;
import com.example.Xtrend_Ai.dto.PodcastLimitResponse;
import com.example.Xtrend_Ai.dto.PodcastRequest;
import com.example.Xtrend_Ai.dto.PodcastResponse;
import com.example.Xtrend_Ai.entity.News;
import com.example.Xtrend_Ai.entity.Podcast;
import com.example.Xtrend_Ai.entity.User;
import com.example.Xtrend_Ai.enums.ContentForm;
import com.example.Xtrend_Ai.enums.Status;
import com.example.Xtrend_Ai.exceptions.ArticleNotFoundException;
import com.example.Xtrend_Ai.exceptions.AudioNotFoundException;
import com.example.Xtrend_Ai.exceptions.PodcastNotFoundException;
import com.example.Xtrend_Ai.exceptions.UsernameNotFoundException;
import com.example.Xtrend_Ai.repository.NewsRepository;
import com.example.Xtrend_Ai.repository.PodcastRepository;
import com.example.Xtrend_Ai.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.jfr.ContentType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.ResponseBytes;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PodcastService {

    private final WebClient client;
    private final PodcastRepository podcastRepository;
    private final NewsRepository newsRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;


    @Value("${bucket.name}")
    private String bucketName;

    public PodcastResponse generatePodcast(PodcastRequest podcastRequest)  {


            News news  = newsRepository.findById(podcastRequest.getNewsId()).orElseThrow(()->new
                    PodcastNotFoundException("News with id " + podcastRequest.getNewsId() + " not found"));

            User user  = userRepository.findByEmail(podcastRequest.getEmail()).orElseThrow(()->
                    new UsernameNotFoundException("User not found"));


            /// check for limit reached before generating
            PodcastLimitResponse limit= PodcastLimitReached(podcastRequest);
            if(limit.getLimitReached().equals(Boolean.TRUE)) {
                return PodcastResponse.builder().build();
            }
            String keyNumber = UUID.randomUUID().toString();

            Podcast podcast = Podcast.builder()
                    .key(keyNumber)
                    .date(LocalDateTime.now().toString())
                    .news(news)
                    .contentForm(podcastRequest.getContentForm())
                    .status(Status.PROCESSING)
                    .user(user)
                    .build();

            podcastRepository.save(podcast);

            try {
                podcastRequest.setArticleUrl(news.getArticle().getLink());

                log.info(" podcast request {}", podcastRequest);


                client.post()
                        .uri("/generate-podcast")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(podcastRequest)
                        .retrieve()
                        .bodyToMono(byte[].class)
                        .subscribe(bytes -> uploadPodcast(bucketName, podcast.getId(), keyNumber,bytes));


            }catch(RuntimeException e){
                throw new RuntimeException("Failed to get podcast from Flask: " + e.getMessage());

                }

            /// returned straight away for client side polling.
        return PodcastResponse.builder()
                .podcastId(podcast.getId())
                .key(podcast.getKey())
                .status(podcast.getStatus())
                .build();
    }




    public void uploadPodcast(String bucket,Long podcastId, String keyNumber, byte[] bytes )  {
        log.info("podcast generated now to be saved in s3");
        if(bytes == null || bytes.length == 0) {
            throw new AudioNotFoundException("audio either null or empty");
        }
        Podcast podcast = podcastRepository.findById(podcastId)
                .orElseThrow(() -> new PodcastNotFoundException("Podcast with ID %s not found".formatted(podcastId)));


        String key = "podcast/audio/%s/%s".formatted(podcastId, keyNumber);
        s3Service.putObject(bucket, key, bytes);
        log.info("saved in s3!!");

        podcast.setStatus(Status.COMPLETED);
        podcastRepository.save(podcast);

    }

    /**
     * used for client side polling, as we check the status of the podcast generation
     * @param podcastId - the podcast that was just created
     * @return a presigned url
     */
    public PodcastResponse podcastStatus (Long podcastId)  {
        Podcast podcast = podcastRepository.findById(podcastId).orElseThrow(()-> new PodcastNotFoundException(
                "podcast with id " + podcastId + " not found"
        ));

        if(podcast.getStatus() == Status.COMPLETED) {
            String audioFile = getPodcast(podcastId);
            return PodcastResponse.builder()
                    .status(podcast.getStatus())
                    .url(audioFile)
                    .build();
        }
        else{
            return PodcastResponse
                    .builder()
                    .status(podcast.getStatus())
                    .build();
        }

    }


    public String getPodcast(Long podcastId) {
        Podcast findPodcast = podcastRepository.findById(podcastId).orElseThrow(()->
                new PodcastNotFoundException("Podcast with id " + podcastId + " not found"));


        String key = "podcast/audio/%s/%s".formatted(findPodcast.getId(), findPodcast.getKey());
        /// presigned url that is a temporary accessible link to our bucket object
        URL audioUrl = s3Service.getPresignedForObject(bucketName,key);

        if(audioUrl != null) {
            return audioUrl.toString();
        }
        else{
            throw new AudioNotFoundException("audio returned from s3 is not found");
        }


    }

    /**
     * this is to ensure a user can generate maximum of 2 podcasts from the same article and same form of content
     * @param podcastRequest - validate if limit has been reached
     * @return Boolean of True if limit reached
     */
    public PodcastLimitResponse PodcastLimitReached(PodcastRequest podcastRequest) {

        User user = userRepository.findByEmail(podcastRequest.getEmail()).
                orElseThrow(()->new UsernameNotFoundException("User not found"));

        News news = newsRepository.findById(podcastRequest.getNewsId()).orElseThrow(()->new ArticleNotFoundException("News with id " + podcastRequest.getNewsId() + " not found"));

        /// user has maximum of 2 generated podcasts for the same article and same Form of content
        /// content can be short Form or Long form
        /// user X can generate long form/short form podcasts from the same article a maximum of twice
        List<Podcast> podcastsGenerated = podcastRepository.findAll()
                .stream()
                .filter(podcast ->podcast.getUser().equals(user) && podcast.getNews().equals(news)
                        && podcast.getContentForm().equals(podcastRequest.getContentForm()))
                .toList();

        if (podcastsGenerated.size() >=10) {
            String alternative;
            if(podcastRequest.getContentForm() == ContentForm.LONG){
                alternative = ContentForm.SHORT.toString();
            }else{
                alternative = ContentForm.LONG.toString();
            }
            return PodcastLimitResponse.builder()
                    .limitReached(true)
                    .message("you have reached the maximum limit for generating podcasts with %s form content However you can still generate podcasts with %s form content"
                            .formatted(podcastRequest.getContentForm(), alternative))
                    .build();

        }else{
            return PodcastLimitResponse.builder()
                    .limitReached(false)
                    .message("you can proceed with podcast generation")
                    .build();
        }


    }













    }



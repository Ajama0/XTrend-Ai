package com.example.Xtrend_Ai.service;


import com.example.Xtrend_Ai.Aws.S3Service;
import com.example.Xtrend_Ai.Mail.MailService;
import com.example.Xtrend_Ai.dto.PodcastLimitResponse;
import com.example.Xtrend_Ai.dto.PodcastRequest;
import com.example.Xtrend_Ai.dto.PodcastResponse;
import com.example.Xtrend_Ai.entity.News;
import com.example.Xtrend_Ai.entity.Podcast;
import com.example.Xtrend_Ai.entity.User;
import com.example.Xtrend_Ai.enums.ContentForm;
import com.example.Xtrend_Ai.enums.PodcastType;
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
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import com.example.Xtrend_Ai.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.ResponseBytes;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
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
    private final MailService mailService;


    @Value("${bucket.name}")
    private String bucketName;


    public PodcastResponse generatePodcastFromNews(PodcastRequest podcastRequest) {

            /// ensure only newsId is present
            if(podcastRequest.getPodcastType().equals(PodcastType.NEWS) && podcastRequest.getNewsId()==null){
                throw new BadRequestException("newsId is required for news podcast");
            }
            News news  = newsRepository.findById(podcastRequest.getNewsId()).orElseThrow(()->new
                    PodcastNotFoundException("News with id " + podcastRequest.getNewsId() + " not found"));

            User user  = userRepository.findByEmail(podcastRequest.getEmail()).orElseThrow(()->
                    new UsernameNotFoundException("User not found"));

            /// check for limit reached before generating
            PodcastLimitResponse limit= podcastLimitReached(podcastRequest);
            if(limit.getLimitReached().equals(Boolean.TRUE)) {
                return PodcastResponse.builder().build();
            }

            String keyNumber = UUID.randomUUID().toString();

            Podcast podcast = Podcast.builder()
                    .key(keyNumber)
                    .date(LocalDateTime.now().toString())
                    .news(news)
                    .podcastType(podcastRequest.getPodcastType())
                    .contentForm(podcastRequest.getContentForm())
                    .status(Status.PROCESSING)
                    .user(user)
                    .build();

            podcastRepository.save(podcast);

            try {
                podcastRequest.setArticleUrl(news.getArticle().getLink());

                log.info(" podcast request {}", podcastRequest);

                log.info("about to call async function");
                client.post()
                        .uri("/api/v1/podcast/create/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(podcastRequest)
                        .retrieve()
                        .bodyToMono(byte[].class)
                        .subscribe(bytes -> uploadPodcast(bucketName, podcast.getId(), keyNumber,bytes));


            }catch(RuntimeException e){
                throw new RuntimeException("Failed to get podcast from Flask: " + e.getMessage());

                }

            /// returned straight away for client side polling.
        log.info("returning data");
        return PodcastResponse.builder()
                .podcastId(podcast.getId())
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

            String bodyFormat = String.format(
                    "Hi %s,\n\n" +
                            "Great news! Your latest AI-generated podcast, is now ready.\n\n" +
                            "Head over to the Rela AI app and check it out under \"My Podcasts\".\n\n" +
                            "Thank you for using Rela AI!\n\n" +
                            "– The Rela AI Team",
                    podcast.getUser().getFirstname()
            );
            mailService.sendMail("abasjama06@gmail.com", bodyFormat, "Your podcast is finally ready!");
            log.info("email sent");

            return PodcastResponse.builder()
                    .status(podcast.getStatus())
                    .podcastId(podcast.getId())
                    .build();
        }
        else{
            return PodcastResponse
                    .builder()
                    .podcastId(podcast.getId())
                    .status(podcast.getStatus())
                    .build();
        }

    }


    public PodcastResponse getPodcast(Long podcastId) {
        Podcast findPodcast = podcastRepository.findById(podcastId).orElseThrow(()->
                new PodcastNotFoundException("Podcast with id " + podcastId + " not found"));

        if(!(findPodcast.getStatus() == Status.COMPLETED)) {
            throw new PodcastNotFoundException("podcast not found in s3, podcast may still be processing");
        }

        String key = "podcast/audio/%s/%s".formatted(findPodcast.getId(), findPodcast.getKey());
        /// presigned url that is a temporary accessible link to our bucket object
        URL audioUrl = s3Service.getPresignedForObject(bucketName,key);

        if(audioUrl != null) {
            return PodcastResponse.builder()
                    .podcastId(findPodcast.getId())
                    .url(audioUrl.toString())
                    .build();
//            return PodcastResponse.builder()
//                    .podcastId(findPodcast.getId())
//                    .url(audioUrl.toString())
//                    .title(findPodcast.getNews().getArticle().getTitle())
//                    .imageUrl(findPodcast.getNews().getArticle().getImage_url())
//                    .country(findPodcast.getNews().getCountry())
//                    .category(findPodcast.getNews().getCategory())
//                    .description(findPodcast.getNews().getArticle().getDescription())
//                    .build();
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
    public PodcastLimitResponse podcastLimitReached(PodcastRequest podcastRequest) {

        User user = userRepository.findByEmail(podcastRequest.getEmail()).
                orElseThrow(()->new UsernameNotFoundException("User not found"));

        /// user has maximum of 2 generated podcasts for each form of content(short or long)
        /// user has can have 2 long form podcasts and 2 short form (free version) for any article
        List<Podcast> podcastsGenerated = podcastRepository.findAll()
                .stream()
                .filter(podcast ->podcast.getUser().equals(user)
                        && podcast.getContentForm().equals(podcastRequest.getContentForm()))
                .toList();

        if (podcastsGenerated.size() >=10000) {
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


    public void deletePodcast(Long podcastId) {
        Podcast podcast = podcastRepository.findById(podcastId).orElseThrow(()->new PodcastNotFoundException("Podcast with id " + podcastId + " not found"));

        if (podcast.getStatus().equals(Status.PROCESSING)){
            throw new IllegalArgumentException("Podcast status is PROCESSING therefore cannot be deleted");
        }
        else{
            String key = "podcast/audio/%s/%s".formatted(podcastId, podcast.getKey());
            s3Service.DeleteObject(bucketName, key);
            podcastRepository.deleteById(podcastId);
        }

    }


    /**
     *
     * @param file - the content to be used for podcast generation
     * @param podcastRequest - DTO that contains type, contentform and user
     * @return - podcast id and status used for polling.
     */
    public PodcastResponse generatePodcastFromPdf(PodcastRequest podcastRequest, MultipartFile file) throws IOException {
        if(podcastRequest.getPodcastType().equals(PodcastType.FILE) && (file ==null || file.isEmpty() )){
            throw new BadRequestException("file is required");
        }

        String filename = getFilename(file);
        byte[] fileBytes = file.getBytes();

        /// before generating, check the user has not reached his limit
        podcastLimitReached(podcastRequest);

        User user = userRepository.findByEmail(podcastRequest.getEmail()).orElseThrow(()->
                new UsernameNotFoundException("User not found"));

        String key = UUID.randomUUID().toString();

        Podcast podcast = Podcast.builder()
                .podcastType(podcastRequest.getPodcastType())
                .news(null)
                .user(user)
                .key(key)
                .contentForm(podcastRequest.getContentForm())
                .status(Status.PROCESSING)
                .build();

        podcastRepository.save(podcast);



        log.info(" CONTENT FORM ISSSS{}" ,podcastRequest.getContentForm());
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", new ByteArrayResource(fileBytes))
                        .filename(filename);
        builder.part("filename", filename);
        builder.part("contentForm", podcastRequest.getContentForm().toString());

        /// we want to make an async call to our podcast api to generate the podcast and save to s3
        client.post()
                .uri("/api/v1/podcast/create/file")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .bodyToMono(byte[].class)
                .doOnError(error -> log.error("Error during async call", error))
                .doOnSuccess(bytes -> log.info("Successfully received response"))
                .subscribe(bytes -> uploadPodcast(bucketName, podcast.getId(), key, bytes));




        /// return response to user to allow polling
        return PodcastResponse
                .builder()
                .podcastId(podcast.getId())
                .status(podcast.getStatus())
                .build();
    }



    private String getFilename( MultipartFile file) {
        String filename = Objects.requireNonNull(file.getOriginalFilename());

        if(!filename.endsWith(".pdf")){
            throw new BadRequestException("files can not be in this format" + filename.substring(filename.indexOf(".")));

        }
        return filename;
    }


    /**
     *
     * @param podcastRequest - request containing the input from the user
     * @return podcastResponse used for polling
     */
    public PodcastResponse generatePodcastFromInput(PodcastRequest podcastRequest) {
        /// ensure input is present
        boolean isTextEmpty = podcastRequest.getText() == null || podcastRequest.getText().isEmpty();
        boolean isUrlEmpty = podcastRequest.getUrl() == null || podcastRequest.getUrl().isEmpty();

        if (isTextEmpty && isUrlEmpty) {
            throw new BadRequestException("Text or URL must be provided for podcast input");
        }

        podcastLimitReached(podcastRequest);

        /// ensure that the raw text input is more than 500 characters if content type is lone
        /// podcast quality seems to be poor if the text input is small and content form is long
        ContentForm contentForm;

        if(!isTextEmpty && podcastRequest.getContentForm().equals(ContentForm.LONG ) && podcastRequest.getText().length()<500){
            contentForm = ContentForm.SHORT;

        }else{
            contentForm = podcastRequest.getContentForm();
        }

        User user = userRepository.findByEmail(podcastRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String key = UUID.randomUUID().toString();

        Podcast podcast = Podcast.builder()
                .podcastType(PodcastType.INPUT)
                .news(null)
                .user(user)
                .key(key)
                .contentForm(contentForm)
                .status(Status.PROCESSING)
                .build();

        podcastRepository.save(podcast);



        Map<String, Object> body = new HashMap<>();
        if (!isTextEmpty) {
            body.put("raw_text", podcastRequest.getText());
        } else {
            body.put("url", podcastRequest.getUrl());
        }
        body.put("contentForm", contentForm.toString());

        client.post()
                .uri("/api/v1/podcast/create/input")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(byte[].class)
                .subscribe(bytes -> uploadPodcast(bucketName, podcast.getId(), key, bytes));

        return PodcastResponse.builder()
                .podcastId(podcast.getId())
                .status(podcast.getStatus())
                .build();
    }
}






package com.example.Xtrend_Ai.controller;

import com.example.Xtrend_Ai.config.cacheConfig;
import com.example.Xtrend_Ai.dto.PodcastRequest;
import com.example.Xtrend_Ai.dto.PodcastResponse;
import com.example.Xtrend_Ai.entity.Podcast;
import com.example.Xtrend_Ai.service.PodcastService;
import lombok.RequiredArgsConstructor;

import okhttp3.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/podcast")
@RequiredArgsConstructor
public class PodcastController {


    private final PodcastService podcastService;
    /**
     * this endpoint recieves a url as we deseralize the json into PocdastRequest which contains a single url
     * we then make a call to our python microservice via an endpoint which handles the podcast generation
     */
    @PostMapping(path = "/create/from/news")
    public ResponseEntity<PodcastResponse> generatePodcast(@RequestBody PodcastRequest podcastRequest){
        PodcastResponse podcastResponse = podcastService.generatePodcastFromNews(podcastRequest);
        return new ResponseEntity<>(podcastResponse, HttpStatus.CREATED);

    }



    @GetMapping(path = "/status/{id}")
    public ResponseEntity<PodcastResponse>PollingStatus(@PathVariable("id")Long PodcastId){
        PodcastResponse podcastResponse = podcastService.podcastStatus(PodcastId);
        return new ResponseEntity<>(podcastResponse, HttpStatus.OK);
    }

    @DeleteMapping(path="delete/{id}")
    public ResponseEntity<Void> deletePodcast(@PathVariable("id")Long PodcastId){
        podcastService.deletePodcast(PodcastId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(path="create/from/file", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<PodcastResponse> createPodcastFromFileInputs(
            @RequestPart(name="podcastInfo") PodcastRequest podcastRequest,
            @RequestPart(name="file") MultipartFile file) throws IOException {

        PodcastResponse response = podcastService.generatePodcastFromPdf(podcastRequest, file);
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }


    @PostMapping(path="create/from/input")
    public ResponseEntity<PodcastResponse> createPodcastFromInputs(@RequestBody PodcastRequest podcastRequest){
        PodcastResponse podcastResponse = podcastService.generatePodcastFromInput(podcastRequest);
        return new ResponseEntity<>(podcastResponse, HttpStatus.CREATED);
    }


    @GetMapping(path="/me")
    public ResponseEntity<List<PodcastResponse>> getUserPodcasts(@RequestParam("username")String email){
        List<PodcastResponse> findUserPodcasts = podcastService.getUserPodcast(email);
        return new ResponseEntity<>(findUserPodcasts, HttpStatus.OK);
    }


    @GetMapping(path="/url/{id}")
    public ResponseEntity<cacheConfig.signedUrl> getCachedSignedUrl(@PathVariable("id") Long podcastId){
        String email = "harry@example.com";
        return new ResponseEntity<>(podcastService.checkIfSignedUrlIsPresentInCache(email, podcastId), HttpStatus.OK);


    }
}

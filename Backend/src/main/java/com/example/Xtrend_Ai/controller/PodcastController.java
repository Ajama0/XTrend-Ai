package com.example.Xtrend_Ai.controller;

import com.example.Xtrend_Ai.dto.PodcastRequest;
import com.example.Xtrend_Ai.dto.PodcastResponse;
import com.example.Xtrend_Ai.entity.Podcast;
import com.example.Xtrend_Ai.service.PodcastService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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


    @GetMapping(path="/download/{id}")
    public ResponseEntity<PodcastResponse>downloadPodcastUrl(@PathVariable("id")Long PodcastId){
        PodcastResponse podcastResponse = podcastService.getPodcast(PodcastId);
        return new ResponseEntity<>(podcastResponse, HttpStatus.OK);
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
            @RequestPart(name="file") MultipartFile file){

        PodcastResponse response = podcastService.generatePodcastFromPdfOrImage(podcastRequest, file);
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }


    @PostMapping(path="create/from/input")
    public ResponseEntity<PodcastResponse> createPodcastFromInputs(@RequestBody PodcastRequest podcastRequest){
        PodcastResponse podcastResponse = podcastService.generatePodcastFromInput(podcastRequest);
        return new ResponseEntity<>(podcastResponse, HttpStatus.CREATED);
    }
}

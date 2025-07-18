package com.example.Xtrend_Ai.controller;

import com.example.Xtrend_Ai.dto.PodcastRequest;
import com.example.Xtrend_Ai.dto.PodcastResponse;
import com.example.Xtrend_Ai.service.PodcastService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/podcast")
@RequiredArgsConstructor
public class PodcastController {


    private final PodcastService podcastService;
    /**
     * this endpoint recieves a url as we deseralize the json into PocdastRequest which contains a single url
     * we then make a call to our python microservice via an endpoint which handles the podcast generation
     */
    @PostMapping(path = "/generate")
    public ResponseEntity<PodcastResponse> generatePodcast(@RequestBody PodcastRequest podcastRequest){
        PodcastResponse podcastResponse = podcastService.generatePodcast(podcastRequest);
        return new ResponseEntity<>(podcastResponse, HttpStatus.CREATED);

    }
}

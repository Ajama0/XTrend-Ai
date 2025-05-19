package com.example.Xtrend_Ai.controller;

import com.example.Xtrend_Ai.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/video")
@RequiredArgsConstructor
public class VideoGenerationController {

    private final VideoService videoService;


    /**
     *
     * @param id - represents the news article for which the user wants to generate a clip
     * @param username - represnets the user who generates the clip
     * @return  Void - nothing is returned as data is instead persisted.
     */
    @PostMapping(path="/generate/{id}/{username}")
    public ResponseEntity<Void> generateVideo(@PathVariable("id") long id, @PathVariable("username") String username) {
        new ResponseEntity<Void>(videoService.generateVideo(id, username), HttpStatus.CREATED);
    }

}

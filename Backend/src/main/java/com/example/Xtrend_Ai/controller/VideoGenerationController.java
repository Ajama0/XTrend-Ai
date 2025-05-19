package com.example.Xtrend_Ai.controller;

import com.example.Xtrend_Ai.dto.AvatarResponse;
import com.example.Xtrend_Ai.dto.VoiceResponse;
import com.example.Xtrend_Ai.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/video")
@RequiredArgsConstructor
public class VideoGenerationController {

    private final VideoService videoService;


    /**
     *
     * @param id - represents the news article for which the user wants to generate a clip
     * @param username - represents the user who generates the clip
     * @return  Void - nothing is returned as data is instead persisted.
     */

    @PostMapping(path="/generate/{id}/{username}")
    public ResponseEntity<Void> generateVideo(@PathVariable("id") long id, @PathVariable("username") String username) {
        videoService.generateVideo(id, username);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @GetMapping(path="/all/avatars")
    private ResponseEntity<AvatarResponse> fetchAvatars(){
        AvatarResponse avatarResponse = videoService.fetchAvatars();
        return new ResponseEntity<>(avatarResponse, HttpStatus.OK);

    }



    @GetMapping(path="/all/voices")
    private ResponseEntity<VoiceResponse> fetchVoices(){
        VoiceResponse voiceResponse = videoService.fetchVoices();
        return new ResponseEntity<>(voiceResponse, HttpStatus.OK);

    }

}

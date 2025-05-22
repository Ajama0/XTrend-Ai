package com.example.Xtrend_Ai.controller;

import com.example.Xtrend_Ai.dto.AvatarResponse;
import com.example.Xtrend_Ai.dto.HeyGenResponse;
import com.example.Xtrend_Ai.dto.HeyGenWebhook;
import com.example.Xtrend_Ai.dto.VoiceResponse;
import com.example.Xtrend_Ai.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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

    @PostMapping(path="/generate/{id}")
    public ResponseEntity<HeyGenResponse> generateVideo(@PathVariable("id") long id, @RequestParam("username") String username) throws IOException {
        return new ResponseEntity<>(videoService.generateVideo(id,username),HttpStatus.CREATED);
    }


    @GetMapping(path="/all/avatars")
    public ResponseEntity<AvatarResponse> fetchAvatars(){
        AvatarResponse avatarResponse = videoService.fetchAvatars();
        return new ResponseEntity<>(avatarResponse, HttpStatus.OK);

    }



    @GetMapping(path="/all/voices")
    public ResponseEntity<VoiceResponse> fetchVoices(){
        VoiceResponse voiceResponse = videoService.fetchVoices();
        return new ResponseEntity<>(voiceResponse, HttpStatus.OK);

    }



    @PostMapping(path="/callback")
    public ResponseEntity<Void> HeyGenCallback(@RequestBody HeyGenWebhook heyGenWebhook ){
        videoService.RecieveVideo(heyGenWebhook);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }




}

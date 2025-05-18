package com.example.Xtrend_Ai.controller;

import com.example.Xtrend_Ai.service.BlogService;
import com.example.Xtrend_Ai.service.DiffBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(path="api/v1/blog")
@RestController
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;
    privaet final DiffBotService diffBotService;


    @GetMapping(path="generate/blog/{id}")
    public ResponseEntity<String> generateBlogPost(@PathVariable("id")Long id, @RequestParam("url") String url,
                                                   @RequestParam("username")String username
    ){
        String text = blogService.generateBlog(id,url,username);
        return new ResponseEntity<>(text, HttpStatus.OK);
    }


    /// testing the diffbot API
    @GetMapping(path="/extract")
    public ResponseEntity<String> extractContent(@RequestParam("url")String url){
        String extractedContent = diffBotService.extractTextFromArticle(url);
        return new ResponseEntity<>(extractedContent, HttpStatus.OK);
    }
}

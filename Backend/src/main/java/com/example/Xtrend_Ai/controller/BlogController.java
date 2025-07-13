package com.example.Xtrend_Ai.controller;

import com.example.Xtrend_Ai.service.BlogService;
import com.example.Xtrend_Ai.service.DiffBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequestMapping(path="api/v1/blog")
@RestController
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;
    private final DiffBotService diffBotService;


    @GetMapping(path="generate/blog/{id}")
    public ResponseEntity<Map<String,String>>generateBlogPost(@PathVariable("id")Long id,
                                                              @RequestParam("username")String username
    ){
        String text = blogService.generateBlog(id,username);
        Map<String,String> map = new HashMap<>();
        map.put("blog",text);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }


    /// testing the diffbot API
    @GetMapping(path="/extract")
    public ResponseEntity<String> extractContent(@RequestParam("url")String url){
        String extractedContent = diffBotService.extractTextFromArticle(url);
        return new ResponseEntity<>(extractedContent, HttpStatus.OK);
    }
}

package com.example.Xtrend_Ai.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/Xtrend")
public class temp {

    @GetMapping(path = "test")
    public ResponseEntity<String> tester() {
        return new ResponseEntity<>("connected", HttpStatus.OK);
    }
}

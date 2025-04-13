package com.example.Xtrend_Ai.controller;


import com.example.Xtrend_Ai.entity.User;
import com.example.Xtrend_Ai.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/XTrend")
@RequiredArgsConstructor
public class login {

    private final LoginService loginService;

    @GetMapping(path = "/test")
    public ResponseEntity<String> tester() {
        return new ResponseEntity<>("connected", HttpStatus.OK);
    }


    @GetMapping(path = "/username")
    public ResponseEntity<User>retrieveTestUser(@RequestParam("name") String firstname){
        return new ResponseEntity<>(loginService.login(firstname), HttpStatus.OK);


    }
}

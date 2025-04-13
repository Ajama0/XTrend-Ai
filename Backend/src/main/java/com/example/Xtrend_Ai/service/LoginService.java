package com.example.Xtrend_Ai.service;


import com.example.Xtrend_Ai.exceptions.UsernameNotFoundException;
import com.example.Xtrend_Ai.entity.User;
import com.example.Xtrend_Ai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;



    public User login(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("Username not found"));
    }
}

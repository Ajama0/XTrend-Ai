package com.example.Xtrend_Ai.config;


import com.example.Xtrend_Ai.enums.Topics;
import com.example.Xtrend_Ai.models.User;
import com.example.Xtrend_Ai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class initialization {


    private final UserRepository userRepository;





    @Bean
    CommandLineRunner commandLineRunner() {
        return args->{

            /**
             * here we can create some default test users. we'll log in based on the user information,
             * users will be redirected based on their interests of news/topics
             */

            User harry = User.builder()
                    .firstname("harry")
                    .lastname("james")
                    .email("harry@example.com")
                    .password("harry1234")
                    .interests(List.of(Topics.FINANCE, Topics.AI, Topics.GAMING))
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();


            User alex = User.builder()
                    .firstname("alex")
                    .lastname("jacob")
                    .email("alex@example.com")
                    .password("alex1234")
                    .interests(List.of(Topics.AUTOMOTIVE, Topics.SCIENCE, Topics.GAMING))
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();


            userRepository.saveAll(List.of(harry, alex));
        };
    }
}

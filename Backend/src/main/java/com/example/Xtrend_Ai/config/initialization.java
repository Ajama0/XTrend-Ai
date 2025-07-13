//package com.example.Xtrend_Ai.config;
//
//
//import com.example.Xtrend_Ai.entity.News;
//import com.example.Xtrend_Ai.entity.VideoContent;
//import com.example.Xtrend_Ai.enums.Topics;
//import com.example.Xtrend_Ai.entity.User;
//import com.example.Xtrend_Ai.repository.NewsRepository;
//import com.example.Xtrend_Ai.repository.UserRepository;
//import com.example.Xtrend_Ai.repository.VideoContentRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Configuration
//@RequiredArgsConstructor
//public class initialization {
//
//
//    private final UserRepository userRepository;
//    private final NewsRepository newsRepository;
//    private final VideoContentRepository videoContentRepository;
//
//    @Bean
//    CommandLineRunner commandLineRunner(VideoContentRepository videoContentRepository) {
//        return args->{
//
//            /**
//             * here we can create some default test users. we'll log in based on the user information,
//             * users will be redirected based on their interests of news/topics
//             */
//
//            videoContentRepository.deleteAll();
//            String url =" https://files2.heygen.ai/aws_pacific/avatar_tmp/ee91865d738f48c5a7d48493186cd3f0/8114182ba6e94292b9b9cb3db46b7c16.mp4?Expires=1749016572&Signature=Id5U-kN7AVU3un3FZzIcMMix3KvhJIklTNI1z9RxTUQXF3QNvZC8lFvg-abCZsw7UwD4tWibc~ak572JR7wr8U8Ybq~w7mBg~mu9bE-yN5Q5FgUeASeePxZczT1DZzPWI5cOt8ypxBJ0XGOZGSTvC17tUp3XEzN2m2eVtaOEzRNQXdI6bM7hfnWeFjtaYcWqJc--n1X99ufx-fIQpA3tBSvliVbJiEb29BPuqiaAQhznCa7R~SXm2ITc7lLDpOPGfaXf4Y6Obs43swFZylvNKBWJ5Af8T9KrA2gz9AEmuUWNAm8qFXxeQGt8gg7osXmSbR5tGFwX8KKKQ4iOuAGA1w__&Key-Pair-Id=K38HBHX5LX3X2H";
//
//
//
//
//
//            News news = newsRepository.findById(14L).orElseThrow(()-> new RuntimeException("news doesnt exist"));
//            VideoContent videoContent =  VideoContent.builder()
//                    .video_url(url)
//                    .news(news)
//                    .user(userRepository.findById(1L).orElseThrow(()-> new RuntimeException("user doesnt exist")))
//                    .build();
//
//
//            videoContentRepository.save(videoContent);
//
////            User harry = User.builder()
////                    .firstname("harry")
////                    .lastname("james")
////                    .email("harry@example.com")
////                    .password("harry1234")
////                    .interests(List.of(Topics.FINANCE, Topics.AI, Topics.GAMING))
////                    .createdAt(LocalDateTime.now())
////                    .updatedAt(LocalDateTime.now())
////                    .build();
////
////
////            User alex = User.builder()
////                    .firstname("alex")
////                    .lastname("jacob")
////                    .email("alex@example.com")
////                    .password("alex1234")
////                    .interests(List.of(Topics.AUTOMOTIVE, Topics.SCIENCE, Topics.GAMING))
////                    .createdAt(LocalDateTime.now())
////                    .updatedAt(LocalDateTime.now())
////                    .build();
////
////
////            userRepository.saveAll(List.of(harry, alex));
//       };
//    }
//}

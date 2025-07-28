package com.example.Xtrend_Ai.service;

import com.example.Xtrend_Ai.Aws.S3Service;
import com.example.Xtrend_Ai.Mail.MailService;
import com.example.Xtrend_Ai.repository.NewsRepository;
import com.example.Xtrend_Ai.repository.PodcastRepository;
import com.example.Xtrend_Ai.repository.UserRepository;
import jdk.jshell.spi.ExecutionControl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PodcastServiceTest {


    @Mock
    private PodcastRepository podcastRepository;

    @Mock
    private S3Service s3Service;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WebClient webClient;

    @Mock
    private MailService mailService;

    @Mock
    private NewsRepository newsRepository;

    private PodcastService underTest;

    @BeforeEach
    void setUp() {
        underTest = new PodcastService(webClient, podcastRepository, newsRepository, userRepository, s3Service, mailService);

    }

    @Test
    void generatePodcast() {
    }

    @Test
    void uploadPodcast() {
    }

    @Test
    void podcastStatus() {
    }

    @Test
    void getPodcast() {
    }

    @Test
    void podcastLimitReached() {
    }

    @Test
    void deletePodcast() {
    }
}
package com.example.Xtrend_Ai.service;

import com.example.Xtrend_Ai.Aws.S3Service;
import com.example.Xtrend_Ai.Mail.MailService;
import com.example.Xtrend_Ai.dto.PodcastLimitResponse;
import com.example.Xtrend_Ai.dto.PodcastRequest;
import com.example.Xtrend_Ai.dto.PodcastResponse;
import com.example.Xtrend_Ai.entity.News;
import com.example.Xtrend_Ai.entity.Podcast;
import com.example.Xtrend_Ai.entity.User;
import com.example.Xtrend_Ai.enums.ContentForm;
import com.example.Xtrend_Ai.enums.PodcastType;
import com.example.Xtrend_Ai.enums.Status;
import com.example.Xtrend_Ai.exceptions.BadRequestException;
import com.example.Xtrend_Ai.repository.NewsRepository;
import com.example.Xtrend_Ai.repository.PodcastRepository;
import com.example.Xtrend_Ai.repository.UserRepository;
import com.example.Xtrend_Ai.utils.Article;
import jdk.jshell.spi.ExecutionControl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        underTest = Mockito.spy(new PodcastService(webClient, podcastRepository, newsRepository, userRepository, s3Service, mailService));

    }

    @Test
    void EnsureUserCannotGeneratePodcastWhenLimitHasBeenExceeded() {
        //given
        News news = new News();
        User user = new User();
        PodcastRequest podcastRequest = PodcastRequest.builder()
                .articleUrl("/times.com")
                .email("testing@example.com")
                .contentForm(ContentForm.LONG)
                .newsId(1L)
                .build();

        PodcastLimitResponse limitResponse = mock(PodcastLimitResponse.class);
        when(newsRepository.findById(any(Long.class))).thenReturn(Optional.of(news));
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));
        doReturn(limitResponse).when(underTest).podcastLimitReached(any(PodcastRequest.class));
        when(limitResponse.getLimitReached()).thenReturn(true);

        //when
        PodcastResponse podcastResponse = underTest.generatePodcastFromNews(podcastRequest);

        //then
        verify(podcastRepository, times(0)).save(any(Podcast.class));
        assertNull(podcastResponse.getStatus());
        assertNull(podcastResponse.getPodcastId());
        assertNull(podcastResponse.getUrl());
    }

    @Test
    void EnsurePodcastIsGeneratedWhenLimitHasNotBeenExceeded() {
        //given
        User user = new User();
        News news = mock(News.class);
        Article article = mock(Article.class);
        WebClient.RequestBodyUriSpec requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);
        Mono<byte[]> mockMono = mock(Mono.class);



        PodcastRequest podcastRequest = PodcastRequest
                .builder()
                .email("tester123@exmaple.com")
                .contentForm(ContentForm.SHORT)
                .newsId(2L)
                .build();
        PodcastLimitResponse limitResponse = mock(PodcastLimitResponse.class);


        when(newsRepository.findById(eq(2L))).thenReturn(Optional.of(news));
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));
        when(news.getArticle()).thenReturn(article);
        when(limitResponse.getLimitReached()).thenReturn(false);
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.contentType(any())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(byte[].class)).thenReturn(mockMono);
        when(mockMono.subscribe(any(Consumer.class)))
                .thenReturn(mock(Disposable.class));


        doReturn(limitResponse).when(underTest).podcastLimitReached(any(PodcastRequest.class));


        //when
        underTest.generatePodcastFromNews(podcastRequest);
        ArgumentCaptor<Podcast> podcastCaptor = ArgumentCaptor.forClass(Podcast.class);

        //then
        verify(podcastRepository,times(1)).save(podcastCaptor.capture());
        verify(webClient, times(1)).post();
        Podcast podcast = podcastCaptor.getValue();

        assertNotNull(podcast);
    }

    @Test
    void uploadPodcast() {

        //given
        String bucketName = "test-bucket";
        String key = "test-key";
        byte[] bytes = new byte[8];
        when(podcastRepository.findById(anyLong())).thenReturn(Optional.of(new Podcast()));

        //when
        underTest.uploadPodcast(bucketName,1L, key, bytes);
        String expectedPath = "podcast/audio/" + 1L + "/" + key;

        //then
        verify(s3Service).putObject(bucketName, expectedPath, bytes);
        verify(podcastRepository).save(any(Podcast.class));
    }

    @Test
    void EmailNotificationSentWhenPodcastStatusIsComplete() {
        //given
        Podcast podcast = mock(Podcast.class);
        User user = mock(User.class);
        when(podcastRepository.findById(anyLong())).thenReturn(Optional.of(podcast));
        when(podcast.getStatus()).thenReturn(Status.COMPLETED);
        when(podcast.getUser()).thenReturn(user);
        when(user.getFirstname()).thenReturn("tester");


        //when
        underTest.podcastStatus(anyLong());

        //then
        verify(mailService).sendMail(anyString(), anyString(),anyString());

    }

    @Test
    void EmailNotificationSentNotSentWhenPodcastStatusIsNotComplete() {
        //given
        Podcast podcast = mock(Podcast.class);
        when(podcastRepository.findById(anyLong())).thenReturn(Optional.of(podcast));

        //when
        underTest.podcastStatus(anyLong());

        //then
        ///ensure this was not called
        verify(mailService, times(0)).sendMail(anyString(), anyString(),anyString());

    }

    @SneakyThrows
    @Test
    void CheckIfWeCanGetPodcastWithoutExceptions() {
        //given
        Podcast podcast = mock(Podcast.class);
        URL url = new URL("https://testing.com");
        when(podcastRepository.findById(anyLong())).thenReturn(Optional.of(podcast));
        when(s3Service.getPresignedForObject(anyString(), anyString())).thenReturn(url);
        when(podcast.getId()).thenReturn(1L);
        when(podcast.getKey()).thenReturn("test-key");
        ReflectionTestUtils.setField(underTest, "bucketName", "test-bucket");

        //when & then
        underTest.getPodcast(1L);

        ArgumentCaptor<String> s3captor = ArgumentCaptor.forClass(String.class);
        verify(s3Service, times(1)).getPresignedForObject(anyString(), s3captor.capture());
        assertEquals("podcast/audio/1/test-key", s3captor.getValue());
    }

    @Test
    void shouldReturnLimitReachedWhenUserHasTenPodcastsWithSameForm() {
        // given
        User user = new User();
        PodcastRequest request = PodcastRequest.builder()
                .email("user@example.com")
                .contentForm(ContentForm.LONG)
                .build();

        List<Podcast> podcasts = IntStream.range(0, 2)
                .mapToObj(i -> {
                    Podcast p = mock(Podcast.class);
                    when(p.getUser()).thenReturn(user);
                    when(p.getContentForm()).thenReturn(ContentForm.LONG);
                    return p;
                })
                .collect(Collectors.toList());

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(podcastRepository.findAll()).thenReturn(podcasts);

        // when
        PodcastLimitResponse response = underTest.podcastLimitReached(request);

        // then
        assertTrue(response.getLimitReached());
        assertTrue(response.getMessage().contains("you have reached the maximum limit"));
    }

    @Test
    void EnsurePodcastCannotBeDeletedDuringProcessing() {
        Podcast podcast = mock(Podcast.class);

        when(podcastRepository.findById(anyLong())).thenReturn(Optional.of(podcast));
        when(podcast.getStatus()).thenReturn(Status.PROCESSING);

        assertThrows(IllegalArgumentException.class, () -> underTest.deletePodcast(1L));

        verify(s3Service,times(0)).DeleteObject(anyString(),anyString());

    }

    @Test
    void GeneratePodcastFromInputWhenInputIsNotValid() {
        //given
        PodcastRequest podcastRequest = mock(PodcastRequest.class);
        when(podcastRequest.getText().isEmpty()).thenReturn(true);
        when(podcastRequest.getUrl().isEmpty()).thenReturn(true);

        //assert/when
        assertThrows(BadRequestException.class, ()-> underTest.generatePodcastFromInput(any(PodcastRequest.class)));

        verify(underTest.podcastLimitReached(any(PodcastRequest.class)), times(0));
    }

    @Test
    void GeneratePodcastFromInputWhenInputIsValid() {

        //given
        PodcastRequest podcastRequest = mock(PodcastRequest.class);
        UserRepository userRepository = mock(UserRepository.class);
        PodcastRepository podcastRepository = mock(PodcastRepository.class);
        WebClient webClient = mock(WebClient.class);

        PodcastRequest request = new PodcastRequest();
        request.setContentForm(ContentForm.LONG);
        request.setPodcastType(PodcastType.INPUT);
        request.setText("something example");



        when(podcastRequest.getText().isEmpty()).thenReturn(false);
        when(podcastRequest.getText()).thenReturn(any(String.class));
        when(podcastRequest.getUrl().isEmpty()).thenReturn(false);
        when(podcastRequest.getUrl()).thenReturn(any(String.class));
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.ofNullable(any(User.class)));


        doNothing().when(underTest).podcastLimitReached(any(PodcastRequest.class));

        //when
         PodcastResponse podcastResponse = underTest.generatePodcastFromInput(request);

        ArgumentCaptor<Podcast> podcast= ArgumentCaptor.forClass(Podcast.class);

        verify(podcastRepository).save(podcast.capture());
        verify(webClient, times(1)).post();

        Podcast saved = podcast.getValue();
        assertEquals(PodcastType.INPUT, saved.getPodcastType());
        //because the input is less than 500 characters
        assertEquals(ContentForm.SHORT, saved.getContentForm());
        assertEquals(Status.PROCESSING, podcastResponse.getStatus());
        assertNotNull(podcastResponse.getPodcastId());




    }
}
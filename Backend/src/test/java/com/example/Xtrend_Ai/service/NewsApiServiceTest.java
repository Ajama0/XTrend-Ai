package com.example.Xtrend_Ai.service;

import com.example.Xtrend_Ai.client.NewsApi.ApiClient;
import com.example.Xtrend_Ai.dto.NewsResponse;
import com.example.Xtrend_Ai.entity.News;
import com.example.Xtrend_Ai.repository.NewsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import okhttp3.HttpUrl;
import okhttp3.ResponseBody;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import retrofit2.http.HTTP;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewsApiServiceTest {


    private NewsApiService underTest;

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private ApiClient apiClient;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        underTest = new NewsApiService(newsRepository, apiClient, objectMapper);
    }

    @SneakyThrows
    @Test
    void getNews() {
        //given
        //mock the response body as it is returned
        ResponseBody responseBody = mock(ResponseBody.class);
        String fakeJsonString = "{\"id\":1,\"title\":\"test\",\"content\":\"test\"}";
        NewsResponse newsResponse = new NewsResponse();

        when(apiClient.fetchTopStories(any(HttpUrl.class))).thenReturn(responseBody);
        when(responseBody.string()).thenReturn(fakeJsonString);
        when(objectMapper.readValue(fakeJsonString, NewsResponse.class)).thenReturn(newsResponse);


        //when
        NewsResponse expectedResponse = underTest.getNews();

        //then
        assertInstanceOf(NewsResponse.class, expectedResponse);
        assertSame(newsResponse, expectedResponse);
        verify(apiClient).fetchTopStories(any(HttpUrl.class));
        verify(objectMapper).readValue(fakeJsonString, NewsResponse.class);


    }

    @Test
    void defineBase() {
    }

    @Test
    void getLatestNews() {
    }

    @Test
    void saveNews() {
    }

    @Test
    void findAllNews() {
    }
}
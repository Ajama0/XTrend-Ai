package com.example.Xtrend_Ai.service;

import com.example.Xtrend_Ai.client.NewsApi.ApiClient;
import com.example.Xtrend_Ai.dto.NewsResponse;
import com.example.Xtrend_Ai.entity.News;
import com.example.Xtrend_Ai.repository.NewsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import okhttp3.HttpUrl;
import okhttp3.ResponseBody;
import org.apache.coyote.BadRequestException;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import retrofit2.http.HTTP;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

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
        underTest = Mockito.spy(new NewsApiService(newsRepository, apiClient, objectMapper));
    }

    @SneakyThrows
    @Test
    void IsNewsReturnedAndInTheCorrectType() {
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

    @SneakyThrows
    @Test
    void CheckIfLatestNewsIsFetchedAndSavedSuccessfully() {
        //given
        NewsResponse expectedNewsResponse = mock(NewsResponse.class);
        ResponseBody responseBody = mock(ResponseBody.class);
        String fakeBody = "{\"id\":1,\"title\":\"test\",\"content\":\"test\"}";
        HttpUrl url = HttpUrl.parse("https://example.com");



        when(apiClient.fetchTopStories(any(HttpUrl.class))).thenReturn(responseBody);
        when(responseBody.string()).thenReturn(fakeBody);
        when(objectMapper.readValue(fakeBody, NewsResponse.class)).thenReturn(expectedNewsResponse);
        when(expectedNewsResponse.getNextPage()).thenReturn(null);
        when(expectedNewsResponse.getNextPage()).thenReturn("p1","p2", "null"); //stop recursion after 3 iterations
        doNothing().when(underTest).saveNews(any(NewsResponse.class));
        //when
        underTest.GetLatestNews(url , null, 1);

        //Capture what is being saved, and make sure it is equal to what we expected
        ArgumentCaptor<NewsResponse> newsResponseCaptor = ArgumentCaptor.forClass(NewsResponse.class);

        //then
        verify(underTest, times(3)).saveNews(newsResponseCaptor.capture());
        verify(apiClient,times(3)).fetchTopStories(any(HttpUrl.class));

        List<NewsResponse> allSaved = newsResponseCaptor.getAllValues();
        //we make sure it was saved correctly as we compare.
        assertEquals(3, allSaved.size());
        assertTrue(allSaved.stream().allMatch(r -> r == expectedNewsResponse));

    }



    @Test
    void CanNotSaveNewsWithEmptyBody() {
        NewsResponse newsResponse = mock(NewsResponse.class);

        //when
        when(newsResponse.getResults()).thenReturn(Collections.emptyList());
        assertThrows(IllegalArgumentException.class, ()->{
            underTest.saveNews(newsResponse);

        });
    }

    @Test
    void aveNewsWithBody() {

        //given


        //when
        underTest.saveNews(any(NewsResponse.class));

        ArgumentCaptor<News> captor = ArgumentCaptor.forClass(News.class);
        verify(newsRepository).save(captor.capture());
        assertSame(any(News.class), captor.getValue());
    }

    @Test
    void findAllNews() {
    }
}
package com.example.Xtrend_Ai.client.NewsApi;

import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


@Component
public class ApiClient {


    private final OkHttpClient Client;



    /**
     * spring now knows which instance of Okhttpclient to inject based on their configs
     * @param Client - the configured http client with timeouts
     */
    public ApiClient(@Qualifier("NewsApiHttpClient") OkHttpClient Client) {
        this.Client = Client;

    }


    /**
     * this allows us to make the api call to news api using our client whilst also passing in a set of queries
     * @param url- set of queries that are passed in the get request
     * @return response body which will be deserialized into a NewsResponse
     * @throws IOException
     */
    public ResponseBody fetchTopStories(HttpUrl url) throws IOException {

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Call call = Client.newCall(request);

        Response response = call.execute();

        return response.body();
    }







//    public Retrofit getRetrofit() {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://newsapi.org/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(httpClient.newBuilder().build())
//                .build();
//
//        return retrofit;
//    }
//
//
//    /**
//     * retrofit defines the implementation for the apiservice class
//     * @return an apiservice object with impl ready to call enqueue to run the http call
//     */
//    public ApiService getApiService() {
//        return getRetrofit().create(ApiService.class);
//    }


}

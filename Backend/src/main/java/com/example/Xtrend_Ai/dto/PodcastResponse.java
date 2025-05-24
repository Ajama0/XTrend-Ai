package com.example.Xtrend_Ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@RequiredArgsConstructor
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PodcastResponse {

    @JsonProperty("data")
    private Data data;

    @lombok.Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Data{
        private String audio;
    }



}

package com.example.Xtrend_Ai.dto;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PodcastResponse {

    private Data data;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Data {
        private String audio;
    }
}

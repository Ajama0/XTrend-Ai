package com.example.Xtrend_Ai.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeyGenResponse {

    @JsonProperty("data")
    private VideoResponse videoResponse;

    @Data
    @Builder
    private static class VideoResponse{
        private String video_id;
    }
}

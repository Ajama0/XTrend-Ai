package com.example.Xtrend_Ai.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PodcastLimitResponse {
    private String message;
    private Boolean limitReached;
}

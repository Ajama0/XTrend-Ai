package com.example.Xtrend_Ai.dto;

import com.example.Xtrend_Ai.enums.Status;
import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PodcastResponse {


    /// use this response to allow client to poll the backend

    private Long podcastId;
    private Status status;

}

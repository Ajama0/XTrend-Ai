package com.example.Xtrend_Ai.dto;

import com.example.Xtrend_Ai.enums.ContentForm;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PodcastRequest {
    private Long newsId;
    /// the user who generated the request
    private String username;

    private ContentForm contentForm;
    private String articleUrl;
}

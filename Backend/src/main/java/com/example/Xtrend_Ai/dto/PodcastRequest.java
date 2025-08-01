package com.example.Xtrend_Ai.dto;

import com.example.Xtrend_Ai.enums.ContentForm;
import com.example.Xtrend_Ai.enums.PodcastType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PodcastRequest {
    /// the user who generated the request
    private String email;

    private ContentForm contentForm;
    private String articleUrl;

    private PodcastType podcastType;


    //this represents the possible ways to generate a podcast, excludes pdf and image which doesn't get serialized into a JSON
    //the frontend will pass in one of these inputs based on what user selects, and the others not used will be ignored
    private Long newsId;
    private String text;
    private String url;
}

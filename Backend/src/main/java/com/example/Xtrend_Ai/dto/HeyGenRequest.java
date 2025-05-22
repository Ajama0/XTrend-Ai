package com.example.Xtrend_Ai.dto;


import com.example.Xtrend_Ai.utils.Dimension;
import com.example.Xtrend_Ai.utils.VideoInputs;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import okhttp3.RequestBody;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HeyGenRequest {

    private boolean caption;

    private String title;

    @JsonProperty("callback_url")
    private String callbackUrl;

    private Dimension dimension;

    @JsonProperty("video_inputs")
    private List<VideoInputs> videoInputs;





}

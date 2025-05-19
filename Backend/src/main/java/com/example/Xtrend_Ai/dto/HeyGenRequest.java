package com.example.Xtrend_Ai.dto;


import com.example.Xtrend_Ai.utils.Dimension;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HeyGenRequest {

    private Boolean caption;

    private String title;

    @JsonProperty("callback_id")
    private String callbackId;

    @Embedded
    private Dimension dimensions;





}

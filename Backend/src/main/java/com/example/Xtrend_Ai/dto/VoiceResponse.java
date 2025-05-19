package com.example.Xtrend_Ai.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class VoiceResponse {


    @JsonIgnoreProperties(ignoreUnknown = true)
    private VoiceData data;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Voices {
        @JsonProperty("voice_id")
        private String voiceId;

        @JsonProperty("language")
        private String language;

        @JsonProperty("gender")
        private String gender;

        @JsonProperty("name")
        private String name;

        @JsonProperty("preview_audio")
        private String previewAudio;

        @JsonProperty("support_pause")
        private Boolean supportPause;

        @JsonProperty("emotion_support")
        private Boolean emotionSupport;

        @JsonProperty("support_locale")
        private Boolean supportLocale;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class VoiceData{
        private List<Voices> voices;

    }

}

package com.example.Xtrend_Ai.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VideoInputs {

    public Character character;
    public Voice voice;
    public Background background;







    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Character{
        private String type;
        private String avatar_id;
        private String avatar_style;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Voice{
        private String type;
        @JsonProperty("input_text")
        private String inputText;
        private String voice_id;
        private String emotion;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Background{
        private String type;
        private String value;

    }
}

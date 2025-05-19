package com.example.Xtrend_Ai.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvatarResponse {


    private List<Avatar> avatars;



    private static class Avatar{
        private String avatar_id;
        private String avatar_name;
        private String gender;
        private String preview_image_url;
        private String preview_video_url;
        private String premium;
    }
}

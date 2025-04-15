package com.example.Xtrend_Ai.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class DiffBotArticle {
    /// this returns the content of the page.
    private String title;
}

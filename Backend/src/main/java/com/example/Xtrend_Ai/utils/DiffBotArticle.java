package com.example.Xtrend_Ai.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DiffBotArticle {
    /// this returns the content of the page.
    private String text;
}

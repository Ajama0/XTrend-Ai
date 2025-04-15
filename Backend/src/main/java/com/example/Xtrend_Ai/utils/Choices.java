package com.example.Xtrend_Ai.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Choices {
    private ChatMessage messages;
}

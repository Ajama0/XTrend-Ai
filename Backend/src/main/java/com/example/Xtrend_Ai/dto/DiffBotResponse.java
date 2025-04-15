package com.example.Xtrend_Ai.dto;

import com.example.Xtrend_Ai.utils.DiffBotArticle;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class DiffBotResponse {

    private List<DiffBotArticle> objects;
}

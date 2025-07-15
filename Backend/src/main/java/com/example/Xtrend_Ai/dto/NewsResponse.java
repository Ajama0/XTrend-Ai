package com.example.Xtrend_Ai.dto;


import com.example.Xtrend_Ai.utils.Article;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewsResponse {

    private String status;
    private int totalResults;
    /// this will be mapped when deserializing
    private List<Article> results;
}

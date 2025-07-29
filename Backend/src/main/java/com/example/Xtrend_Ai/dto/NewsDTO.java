package com.example.Xtrend_Ai.dto;


import com.example.Xtrend_Ai.utils.Article;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NewsDTO {

    private List<Article> article;
}

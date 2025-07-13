package com.example.Xtrend_Ai.dto;


import com.example.Xtrend_Ai.utils.Article;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class NewsResponse {

    private String status;
    private int totalPages;
    private List<Article> articles;
}

package com.example.Xtrend_Ai.dto;

import jakarta.persistence.Access;
import jakarta.persistence.DiscriminatorColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewsRequest {

    private String category, sources, q, pageSize, page, country, language;


}

package com.example.Xtrend_Ai.dto;

import jakarta.persistence.Access;
import jakarta.persistence.DiscriminatorColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class NewsRequest {


    private List<String> category;
    private List<String> country;
    private String timeFrame;
    private String Language;
    private String priorityDomain;
    private String fullContent;
    private String image;
    private String removeDuplicate;



}

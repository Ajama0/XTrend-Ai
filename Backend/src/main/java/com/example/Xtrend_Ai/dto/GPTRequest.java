package com.example.Xtrend_Ai.dto;

import com.example.Xtrend_Ai.utils.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GPTRequest {
    private String model;
    private List<ChatMessage> messages;
    private double temperature;
    private int max_tokens;
}

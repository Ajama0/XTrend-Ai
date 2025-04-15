package com.example.Xtrend_Ai.dto;

import com.example.Xtrend_Ai.utils.Choices;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GptResponse {
    private List<Choices> choices;

    /**
     * Example response from gpt
     * {
     *   "id": "chatcmpl-123",
     *   "object": "chat.completion",
     *   "created": 1677652288,
     *   "choices": [{
     *     "index": 0,
     *     "message": {
     *       "role": "assistant",
     *       "content": "\n\nHello there, how may I assist you today?"
     *     },
     *     "finish_reason": "stop"
     *   }],
     *   "usage": {
     *     "prompt_tokens": 9,
     *     "completion_tokens": 12,
     *     "total_tokens": 21
     *   }
     * }
     * Copy
     */

}

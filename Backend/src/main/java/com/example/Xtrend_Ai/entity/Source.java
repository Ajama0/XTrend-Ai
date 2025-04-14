package com.example.Xtrend_Ai.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import lombok.Data;

@Embeddable
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Source{

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    @Lob
    private String name;
}

package com.example.Xtrend_Ai.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;

@Embeddable
public class Source{
    private int source_id;
    @Lob
    private String name;
}

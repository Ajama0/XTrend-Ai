package com.example.Xtrend_Ai.utils;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


public class NewsUtils {

    public static Map<String,Object> generateQuery(String apiKey){
        Map<String,Object> query = new HashMap<>();
        query.put("apiKey",apiKey);
        return query;
    }
}

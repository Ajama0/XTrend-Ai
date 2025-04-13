package com.example.Xtrend_Ai.utils;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


public class NewsUtils {

    public static Map<String,String> generateQuery(String apiKey){
        Map<String,String> query = new HashMap<>();
        query.put("api_key",apiKey);
        return query;
    }
}

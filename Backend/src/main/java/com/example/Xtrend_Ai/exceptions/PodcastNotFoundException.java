package com.example.Xtrend_Ai.exceptions;

public class PodcastNotFoundException extends RuntimeException {
    public PodcastNotFoundException(String message) {
        super(message);
    }
}

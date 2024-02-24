package com.nextuple.pranay.fullstack.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorDetails {
    private LocalDateTime timeStamp;
    private final String error;
    private final String message;
    private final String description;

    public ErrorDetails(String error, String message, String description) {
        this.error = error;
        this.message = message;
        this.description = description;
        updateTimeStamp();
    }

    public void updateTimeStamp() {
        this.timeStamp = LocalDateTime.now();
    }

}

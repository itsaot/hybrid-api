package com.example.hybridapi.dto;

public record EmailSendResponse(
        boolean success,
        String provider,
        String message
) {
}

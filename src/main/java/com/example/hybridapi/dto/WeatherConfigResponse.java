package com.example.hybridapi.dto;

public record WeatherConfigResponse(
        String provider,
        String baseUrl,
        String apiKeyPreview,
        boolean enabled
) {
}

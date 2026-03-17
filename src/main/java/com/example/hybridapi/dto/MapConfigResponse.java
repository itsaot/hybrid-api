package com.example.hybridapi.dto;

public record MapConfigResponse(
        String provider,
        String geocodeBaseUrl,
        String tilesBaseUrl,
        String routeBaseUrl,
        String apiKeyPreview,
        boolean enabled
) {
}

package com.example.hybridapi.dto;

public record EmailConfigResponse(
        String provider,
        String baseUrl,
        String fromAddress,
        String apiKeyPreview,
        boolean enabled,
        boolean configured
) {
}

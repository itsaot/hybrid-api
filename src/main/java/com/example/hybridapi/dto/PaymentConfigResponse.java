package com.example.hybridapi.dto;

public record PaymentConfigResponse(
        String provider,
        String baseUrl,
        String publicKey,
        String secretKeyPreview,
        String webhookUrl,
        boolean configured
) {
}

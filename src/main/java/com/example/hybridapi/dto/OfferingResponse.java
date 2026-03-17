package com.example.hybridapi.dto;

import java.math.BigDecimal;

import com.example.hybridapi.model.OfferingType;

public record OfferingResponse(
        Long id,
        Long providerId,
        String providerName,
        String name,
        String description,
        OfferingType type,
        BigDecimal price,
        String currency,
        Integer availableUnits,
        Integer durationMinutes,
        boolean active
) {
}

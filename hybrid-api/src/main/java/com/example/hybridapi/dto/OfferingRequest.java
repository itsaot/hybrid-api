package com.example.hybridapi.dto;

import java.math.BigDecimal;

import com.example.hybridapi.model.OfferingType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OfferingRequest(
        @NotNull Long providerId,
        @NotBlank String name,
        String description,
        @NotNull OfferingType type,
        @NotNull @DecimalMin("0.0") BigDecimal price,
        @NotBlank String currency,
        @NotNull @Min(0) Integer availableUnits,
        @NotNull @Min(0) Integer durationMinutes,
        Boolean active
) {
}

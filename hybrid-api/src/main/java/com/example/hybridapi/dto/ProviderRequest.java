package com.example.hybridapi.dto;

import com.example.hybridapi.model.BusinessCategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProviderRequest(
        @NotBlank String name,
        @NotNull BusinessCategory category,
        @NotBlank String city,
        String description,
        boolean supportsGoods,
        boolean supportsServices,
        Boolean active
) {
}

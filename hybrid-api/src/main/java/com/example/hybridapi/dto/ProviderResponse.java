package com.example.hybridapi.dto;

import com.example.hybridapi.model.BusinessCategory;

public record ProviderResponse(
        Long id,
        String name,
        BusinessCategory category,
        String city,
        String description,
        boolean supportsGoods,
        boolean supportsServices,
        boolean active
) {
}

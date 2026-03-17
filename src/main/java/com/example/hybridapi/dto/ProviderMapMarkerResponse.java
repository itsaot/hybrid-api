package com.example.hybridapi.dto;

import com.example.hybridapi.model.BusinessCategory;

public record ProviderMapMarkerResponse(
        Long id,
        String name,
        BusinessCategory category,
        String city,
        String addressLine,
        Double latitude,
        Double longitude
) {
}

package com.example.hybridapi.dto;

import java.util.List;

public record MapSearchResponse(
        String provider,
        List<MapLocation> results
) {
    public record MapLocation(
            String name,
            Double latitude,
            Double longitude,
            String displayAddress
    ) {
    }
}

package com.example.hybridapi.dto;

import java.util.List;

public record RouteResponse(
        String provider,
        double distanceMeters,
        double durationSeconds,
        List<RoutePoint> geometry
) {
    public record RoutePoint(
            double latitude,
            double longitude
    ) {
    }
}

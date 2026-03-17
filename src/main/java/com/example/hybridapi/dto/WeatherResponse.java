package com.example.hybridapi.dto;

public record WeatherResponse(
        double latitude,
        double longitude,
        String timezone,
        String units,
        CurrentWeather current
) {
    public record CurrentWeather(
            String time,
            Double temperature,
            Integer weatherCode,
            Double windSpeed,
            Integer windDirection,
            Boolean isDay
    ) {
    }
}

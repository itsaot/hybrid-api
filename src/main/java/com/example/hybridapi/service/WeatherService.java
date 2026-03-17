package com.example.hybridapi.service;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import com.example.hybridapi.config.WeatherProperties;
import com.example.hybridapi.dto.WeatherConfigResponse;
import com.example.hybridapi.dto.WeatherResponse;

@Service
public class WeatherService {

    private final WeatherProperties weatherProperties;
    private final RestClient.Builder restClientBuilder;

    public WeatherService(WeatherProperties weatherProperties, RestClient.Builder restClientBuilder) {
        this.weatherProperties = weatherProperties;
        this.restClientBuilder = restClientBuilder;
    }

    public WeatherConfigResponse getConfig() {
        return new WeatherConfigResponse(
                weatherProperties.getProvider(),
                weatherProperties.getBaseUrl(),
                weatherProperties.getMaskedApiKey(),
                weatherProperties.isEnabled()
        );
    }

    @SuppressWarnings("unchecked")
    public WeatherResponse getCurrentWeather(double latitude, double longitude) {
        if (!weatherProperties.isEnabled()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Weather integration is disabled");
        }

        RestClient client = restClientBuilder.baseUrl(weatherProperties.getBaseUrl()).build();
        Map<String, Object> payload = client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/forecast")
                        .queryParam("latitude", latitude)
                        .queryParam("longitude", longitude)
                        .queryParam("current", "temperature_2m,weather_code,wind_speed_10m,wind_direction_10m,is_day")
                        .build())
                .retrieve()
                .body(Map.class);

        Map<String, Object> current = (Map<String, Object>) payload.get("current");
        Map<String, Object> currentUnits = (Map<String, Object>) payload.get("current_units");

        return new WeatherResponse(
                ((Number) payload.get("latitude")).doubleValue(),
                ((Number) payload.get("longitude")).doubleValue(),
                String.valueOf(payload.get("timezone")),
                String.valueOf(currentUnits.get("temperature_2m")),
                new WeatherResponse.CurrentWeather(
                        String.valueOf(current.get("time")),
                        toDouble(current.get("temperature_2m")),
                        toInteger(current.get("weather_code")),
                        toDouble(current.get("wind_speed_10m")),
                        toInteger(current.get("wind_direction_10m")),
                        toBoolean(current.get("is_day"))
                )
        );
    }

    private Double toDouble(Object value) {
        return value instanceof Number number ? number.doubleValue() : null;
    }

    private Integer toInteger(Object value) {
        return value instanceof Number number ? number.intValue() : null;
    }

    private Boolean toBoolean(Object value) {
        if (value instanceof Boolean bool) {
            return bool;
        }
        if (value instanceof Number number) {
            return number.intValue() == 1;
        }
        return null;
    }
}

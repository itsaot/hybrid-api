package com.example.hybridapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.hybridapi.dto.MapConfigResponse;
import com.example.hybridapi.dto.MapSearchResponse;
import com.example.hybridapi.dto.ProviderMapMarkerResponse;
import com.example.hybridapi.dto.RouteResponse;
import com.example.hybridapi.dto.WeatherConfigResponse;
import com.example.hybridapi.dto.WeatherResponse;
import com.example.hybridapi.service.MapService;
import com.example.hybridapi.service.WeatherService;

import java.util.List;

@RestController
@RequestMapping("/api/integrations")
public class IntegrationController {

    private final WeatherService weatherService;
    private final MapService mapService;

    public IntegrationController(WeatherService weatherService, MapService mapService) {
        this.weatherService = weatherService;
        this.mapService = mapService;
    }

    @GetMapping("/weather/config")
    public WeatherConfigResponse getWeatherConfig() {
        return weatherService.getConfig();
    }

    @GetMapping("/weather/current")
    public WeatherResponse getCurrentWeather(@RequestParam double latitude, @RequestParam double longitude) {
        return weatherService.getCurrentWeather(latitude, longitude);
    }

    @GetMapping("/maps/config")
    public MapConfigResponse getMapConfig() {
        return mapService.getConfig();
    }

    @GetMapping("/maps/search")
    public MapSearchResponse searchLocations(@RequestParam String query) {
        return mapService.search(query);
    }

    @GetMapping("/maps/providers")
    public List<ProviderMapMarkerResponse> getProviderMarkers(@RequestParam(required = false) String query) {
        return mapService.getProviderMarkers(query);
    }

    @GetMapping("/maps/route")
    public RouteResponse getRoute(
            @RequestParam double startLatitude,
            @RequestParam double startLongitude,
            @RequestParam double endLatitude,
            @RequestParam double endLongitude
    ) {
        return mapService.getRoute(startLatitude, startLongitude, endLatitude, endLongitude);
    }
}

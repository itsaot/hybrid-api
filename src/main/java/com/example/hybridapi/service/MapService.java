package com.example.hybridapi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import com.example.hybridapi.config.MapProperties;
import com.example.hybridapi.dto.MapConfigResponse;
import com.example.hybridapi.dto.MapSearchResponse;
import com.example.hybridapi.dto.ProviderMapMarkerResponse;
import com.example.hybridapi.dto.RouteResponse;

@Service
public class MapService {

    private final MapProperties mapProperties;
    private final RestClient.Builder restClientBuilder;
    private final ProviderService providerService;

    public MapService(MapProperties mapProperties, RestClient.Builder restClientBuilder, ProviderService providerService) {
        this.mapProperties = mapProperties;
        this.restClientBuilder = restClientBuilder;
        this.providerService = providerService;
    }

    public MapConfigResponse getConfig() {
        return new MapConfigResponse(
                mapProperties.getProvider(),
                mapProperties.getGeocodeBaseUrl(),
                mapProperties.getTilesBaseUrl(),
                mapProperties.getRouteBaseUrl(),
                mapProperties.getMaskedApiKey(),
                mapProperties.isEnabled()
        );
    }

    public MapSearchResponse search(String query) {
        if (!mapProperties.isEnabled()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Map integration is disabled");
        }

        RestClient client = restClientBuilder.baseUrl(mapProperties.getGeocodeBaseUrl()).build();
        List<Map> payload = client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("q", query)
                        .queryParam("format", "jsonv2")
                        .queryParam("limit", 5)
                        .build())
                .header("User-Agent", "hybrid-api-test-app/1.0")
                .retrieve()
                .body(List.class);

        List<MapSearchResponse.MapLocation> results = payload.stream()
                .map(this::toLocation)
                .toList();

        return new MapSearchResponse(mapProperties.getProvider(), results);
    }

    public List<ProviderMapMarkerResponse> getProviderMarkers(String query) {
        return providerService.searchProviders(query).stream()
                .filter(provider -> provider.getLatitude() != null && provider.getLongitude() != null)
                .map(provider -> new ProviderMapMarkerResponse(
                        provider.getId(),
                        provider.getName(),
                        provider.getCategory(),
                        provider.getCity(),
                        provider.getAddressLine(),
                        provider.getLatitude(),
                        provider.getLongitude()
                ))
                .toList();
    }

    @SuppressWarnings("unchecked")
    public RouteResponse getRoute(double startLatitude, double startLongitude, double endLatitude, double endLongitude) {
        if (!mapProperties.isEnabled()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Map integration is disabled");
        }

        RestClient client = restClientBuilder.baseUrl(mapProperties.getRouteBaseUrl()).build();
        Map<String, Object> payload = client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/route/v1/driving/{coords}")
                        .queryParam("overview", "full")
                        .queryParam("geometries", "geojson")
                        .build("%s,%s;%s,%s".formatted(startLongitude, startLatitude, endLongitude, endLatitude)))
                .header("User-Agent", "hybrid-api-test-app/1.0")
                .retrieve()
                .body(Map.class);

        List<Map<String, Object>> routes = (List<Map<String, Object>>) payload.get("routes");
        if (routes == null || routes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No route found");
        }

        Map<String, Object> route = routes.get(0);
        Map<String, Object> geometry = (Map<String, Object>) route.get("geometry");
        List<List<Number>> coordinates = (List<List<Number>>) geometry.get("coordinates");

        List<RouteResponse.RoutePoint> points = new ArrayList<>();
        for (List<Number> coordinate : coordinates) {
            points.add(new RouteResponse.RoutePoint(
                    coordinate.get(1).doubleValue(),
                    coordinate.get(0).doubleValue()
            ));
        }

        return new RouteResponse(
                mapProperties.getProvider(),
                ((Number) route.get("distance")).doubleValue(),
                ((Number) route.get("duration")).doubleValue(),
                points
        );
    }

    private MapSearchResponse.MapLocation toLocation(Map payload) {
        return new MapSearchResponse.MapLocation(
                String.valueOf(payload.get("name")),
                parseDouble(payload.get("lat")),
                parseDouble(payload.get("lon")),
                String.valueOf(payload.get("display_name"))
        );
    }

    private Double parseDouble(Object value) {
        if (value == null) {
            return null;
        }
        return Double.parseDouble(String.valueOf(value));
    }
}

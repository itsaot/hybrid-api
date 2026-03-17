package com.example.hybridapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "maps")
public class MapProperties {

    private String provider;
    private String geocodeBaseUrl;
    private String tilesBaseUrl;
    private String routeBaseUrl;
    private String apiKey;
    private boolean enabled;

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getGeocodeBaseUrl() {
        return geocodeBaseUrl;
    }

    public void setGeocodeBaseUrl(String geocodeBaseUrl) {
        this.geocodeBaseUrl = geocodeBaseUrl;
    }

    public String getTilesBaseUrl() {
        return tilesBaseUrl;
    }

    public void setTilesBaseUrl(String tilesBaseUrl) {
        this.tilesBaseUrl = tilesBaseUrl;
    }

    public String getRouteBaseUrl() {
        return routeBaseUrl;
    }

    public void setRouteBaseUrl(String routeBaseUrl) {
        this.routeBaseUrl = routeBaseUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getMaskedApiKey() {
        if (apiKey == null || apiKey.isBlank()) {
            return "not-set";
        }
        if (apiKey.length() <= 6) {
            return "set";
        }
        return apiKey.substring(0, 4) + "..." + apiKey.substring(apiKey.length() - 2);
    }
}

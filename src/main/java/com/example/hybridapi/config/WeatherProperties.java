package com.example.hybridapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "weather")
public class WeatherProperties {

    private String provider;
    private String baseUrl;
    private String apiKey;
    private boolean enabled;

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
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

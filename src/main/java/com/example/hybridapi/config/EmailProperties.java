package com.example.hybridapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "email")
public class EmailProperties {

    private String provider;
    private String baseUrl;
    private String apiKey;
    private String fromAddress;
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

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isConfigured() {
        return enabled && notBlank(provider) && notBlank(baseUrl) && notBlank(apiKey) && notBlank(fromAddress);
    }

    public String getMaskedApiKey() {
        return mask(apiKey);
    }

    private String mask(String value) {
        if (!notBlank(value) || value.length() <= 6) {
            return "not-set";
        }
        return value.substring(0, 4) + "..." + value.substring(value.length() - 2);
    }

    private boolean notBlank(String value) {
        return value != null && !value.isBlank();
    }
}

package com.example.hybridapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "payment")
public class PaymentProperties {

    private String provider;
    private String baseUrl;
    private String publicKey;
    private String secretKey;
    private String webhookUrl;

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

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getWebhookUrl() {
        return webhookUrl;
    }

    public void setWebhookUrl(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public boolean isConfigured() {
        return notBlank(provider) && notBlank(baseUrl) && notBlank(publicKey) && notBlank(secretKey);
    }

    public String getMaskedSecretKey() {
        if (!notBlank(secretKey) || secretKey.length() <= 6) {
            return "not-set";
        }
        return secretKey.substring(0, 4) + "..." + secretKey.substring(secretKey.length() - 2);
    }

    private boolean notBlank(String value) {
        return value != null && !value.isBlank();
    }
}

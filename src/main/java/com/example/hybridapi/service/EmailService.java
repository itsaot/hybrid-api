package com.example.hybridapi.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import com.example.hybridapi.config.EmailProperties;
import com.example.hybridapi.dto.EmailConfigResponse;
import com.example.hybridapi.dto.EmailSendResponse;
import com.example.hybridapi.dto.EmailTestRequest;

@Service
public class EmailService {

    private final EmailProperties emailProperties;
    private final RestClient.Builder restClientBuilder;

    public EmailService(EmailProperties emailProperties, RestClient.Builder restClientBuilder) {
        this.emailProperties = emailProperties;
        this.restClientBuilder = restClientBuilder;
    }

    public EmailConfigResponse getConfig() {
        return new EmailConfigResponse(
                emailProperties.getProvider(),
                emailProperties.getBaseUrl(),
                emailProperties.getFromAddress(),
                emailProperties.getMaskedApiKey(),
                emailProperties.isEnabled(),
                emailProperties.isConfigured()
        );
    }

    public EmailSendResponse sendTestEmail(EmailTestRequest request) {
        if (!emailProperties.isConfigured()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email provider is not fully configured");
        }

        RestClient client = restClientBuilder
                .baseUrl(emailProperties.getBaseUrl())
                .defaultHeader("Authorization", "Bearer " + emailProperties.getApiKey())
                .build();

        client.post()
                .uri("/emails")
                .body(Map.of(
                        "from", emailProperties.getFromAddress(),
                        "to", List.of(request.to()),
                        "subject", request.subject(),
                        "html", request.html()
                ))
                .retrieve()
                .toBodilessEntity();

        return new EmailSendResponse(true, emailProperties.getProvider(), "Test email request sent successfully");
    }
}

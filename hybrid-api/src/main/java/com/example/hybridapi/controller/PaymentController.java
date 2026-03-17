package com.example.hybridapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hybridapi.config.PaymentProperties;
import com.example.hybridapi.dto.PaymentConfigResponse;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentProperties paymentProperties;

    public PaymentController(PaymentProperties paymentProperties) {
        this.paymentProperties = paymentProperties;
    }

    @GetMapping("/config")
    public PaymentConfigResponse getPaymentConfig() {
        return new PaymentConfigResponse(
                paymentProperties.getProvider(),
                paymentProperties.getBaseUrl(),
                paymentProperties.getPublicKey(),
                paymentProperties.getMaskedSecretKey(),
                paymentProperties.getWebhookUrl(),
                paymentProperties.isConfigured()
        );
    }
}

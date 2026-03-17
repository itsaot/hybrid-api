package com.example.hybridapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.hybridapi.dto.EmailConfigResponse;
import com.example.hybridapi.dto.EmailSendResponse;
import com.example.hybridapi.dto.EmailTestRequest;
import com.example.hybridapi.service.EmailService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/config")
    public EmailConfigResponse getConfig() {
        return emailService.getConfig();
    }

    @PostMapping("/test")
    @ResponseStatus(HttpStatus.OK)
    public EmailSendResponse sendTestEmail(@Valid @RequestBody EmailTestRequest request) {
        return emailService.sendTestEmail(request);
    }
}

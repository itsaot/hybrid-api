package com.example.hybridapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailTestRequest(
        @Email @NotBlank String to,
        @NotBlank String subject,
        @NotBlank String html
) {
}

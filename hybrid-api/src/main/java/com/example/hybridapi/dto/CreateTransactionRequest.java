package com.example.hybridapi.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record CreateTransactionRequest(
        @NotBlank String customerName,
        @NotBlank @Email String customerEmail,
        @NotEmpty List<@Valid TransactionLineRequest> lines
) {
}

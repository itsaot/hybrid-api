package com.example.hybridapi.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record TransactionLineRequest(
        @NotNull Long offeringId,
        @NotNull @Min(1) Integer quantity,
        LocalDate serviceDate,
        String notes
) {
}

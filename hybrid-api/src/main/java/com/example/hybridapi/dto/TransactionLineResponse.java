package com.example.hybridapi.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.hybridapi.model.OfferingType;

public record TransactionLineResponse(
        Long offeringId,
        String offeringName,
        OfferingType offeringType,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal lineTotal,
        LocalDate serviceDate,
        String notes
) {
}

package com.example.hybridapi.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import com.example.hybridapi.model.TransactionStatus;
import com.example.hybridapi.model.TransactionType;

public record TransactionResponse(
        Long id,
        String customerName,
        String customerEmail,
        TransactionType transactionType,
        TransactionStatus status,
        BigDecimal totalAmount,
        OffsetDateTime createdAt,
        List<TransactionLineResponse> lines
) {
}

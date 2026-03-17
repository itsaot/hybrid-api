package com.example.hybridapi.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.hybridapi.dto.OfferingResponse;
import com.example.hybridapi.dto.ProviderResponse;
import com.example.hybridapi.dto.TransactionLineResponse;
import com.example.hybridapi.dto.TransactionResponse;
import com.example.hybridapi.model.CustomerTransaction;
import com.example.hybridapi.model.Offering;
import com.example.hybridapi.model.Provider;
import com.example.hybridapi.model.TransactionLine;

@Component
public class ApiMapper {

    public ProviderResponse toResponse(Provider provider) {
        return new ProviderResponse(
                provider.getId(),
                provider.getName(),
                provider.getCategory(),
                provider.getCity(),
                provider.getDescription(),
                provider.isSupportsGoods(),
                provider.isSupportsServices(),
                provider.isActive()
        );
    }

    public OfferingResponse toResponse(Offering offering) {
        return new OfferingResponse(
                offering.getId(),
                offering.getProvider().getId(),
                offering.getProvider().getName(),
                offering.getName(),
                offering.getDescription(),
                offering.getType(),
                offering.getPrice(),
                offering.getCurrency(),
                offering.getAvailableUnits(),
                offering.getDurationMinutes(),
                offering.isActive()
        );
    }

    public TransactionResponse toResponse(CustomerTransaction transaction) {
        List<TransactionLineResponse> lines = transaction.getLines().stream()
                .map(this::toResponse)
                .toList();

        return new TransactionResponse(
                transaction.getId(),
                transaction.getCustomerName(),
                transaction.getCustomerEmail(),
                transaction.getTransactionType(),
                transaction.getStatus(),
                transaction.getTotalAmount(),
                transaction.getCreatedAt(),
                lines
        );
    }

    private TransactionLineResponse toResponse(TransactionLine line) {
        BigDecimal lineTotal = line.getUnitPrice().multiply(BigDecimal.valueOf(line.getQuantity()));
        return new TransactionLineResponse(
                line.getOffering().getId(),
                line.getOffering().getName(),
                line.getOffering().getType(),
                line.getQuantity(),
                line.getUnitPrice(),
                lineTotal,
                line.getServiceDate(),
                line.getNotes()
        );
    }
}

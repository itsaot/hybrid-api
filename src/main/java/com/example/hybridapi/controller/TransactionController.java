package com.example.hybridapi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.hybridapi.dto.CreateTransactionRequest;
import com.example.hybridapi.dto.TransactionResponse;
import com.example.hybridapi.service.ApiMapper;
import com.example.hybridapi.service.TransactionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final ApiMapper apiMapper;

    public TransactionController(TransactionService transactionService, ApiMapper apiMapper) {
        this.transactionService = transactionService;
        this.apiMapper = apiMapper;
    }

    @GetMapping
    public List<TransactionResponse> getTransactions() {
        return transactionService.getTransactions().stream()
                .map(apiMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public TransactionResponse getTransaction(@PathVariable Long id) {
        return apiMapper.toResponse(transactionService.getTransaction(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse createTransaction(@Valid @RequestBody CreateTransactionRequest request) {
        return apiMapper.toResponse(transactionService.createTransaction(request));
    }
}

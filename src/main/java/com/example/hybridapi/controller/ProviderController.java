package com.example.hybridapi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.hybridapi.dto.ProviderRequest;
import com.example.hybridapi.dto.ProviderResponse;
import com.example.hybridapi.model.BusinessCategory;
import com.example.hybridapi.service.ApiMapper;
import com.example.hybridapi.service.ProviderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/providers")
public class ProviderController {

    private final ProviderService providerService;
    private final ApiMapper apiMapper;

    public ProviderController(ProviderService providerService, ApiMapper apiMapper) {
        this.providerService = providerService;
        this.apiMapper = apiMapper;
    }

    @GetMapping
    public List<ProviderResponse> getProviders(@RequestParam(required = false) BusinessCategory category) {
        return providerService.getProviders(category).stream()
                .map(apiMapper::toResponse)
                .toList();
    }

    @GetMapping("/search")
    public List<ProviderResponse> searchProviders(@RequestParam String query) {
        return providerService.searchProviders(query).stream()
                .map(apiMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ProviderResponse getProvider(@PathVariable Long id) {
        return apiMapper.toResponse(providerService.getProvider(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProviderResponse createProvider(@Valid @RequestBody ProviderRequest request) {
        return apiMapper.toResponse(providerService.createProvider(request));
    }

    @PutMapping("/{id}")
    public ProviderResponse updateProvider(@PathVariable Long id, @Valid @RequestBody ProviderRequest request) {
        return apiMapper.toResponse(providerService.updateProvider(id, request));
    }
}

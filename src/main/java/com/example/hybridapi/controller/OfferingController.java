package com.example.hybridapi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.hybridapi.dto.OfferingRequest;
import com.example.hybridapi.dto.OfferingResponse;
import com.example.hybridapi.model.OfferingType;
import com.example.hybridapi.service.ApiMapper;
import com.example.hybridapi.service.OfferingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/offerings")
public class OfferingController {

    private final OfferingService offeringService;
    private final ApiMapper apiMapper;

    public OfferingController(OfferingService offeringService, ApiMapper apiMapper) {
        this.offeringService = offeringService;
        this.apiMapper = apiMapper;
    }

    @GetMapping
    public List<OfferingResponse> getOfferings(
            @RequestParam(required = false) Long providerId,
            @RequestParam(required = false) OfferingType type
    ) {
        return offeringService.getOfferings(providerId, type).stream()
                .map(apiMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public OfferingResponse getOffering(@PathVariable Long id) {
        return apiMapper.toResponse(offeringService.getOffering(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OfferingResponse createOffering(@Valid @RequestBody OfferingRequest request) {
        return apiMapper.toResponse(offeringService.createOffering(request));
    }
}

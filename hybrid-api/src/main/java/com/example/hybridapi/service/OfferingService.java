package com.example.hybridapi.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.hybridapi.dto.OfferingRequest;
import com.example.hybridapi.model.Offering;
import com.example.hybridapi.model.OfferingType;
import com.example.hybridapi.model.Provider;
import com.example.hybridapi.repository.OfferingRepository;

@Service
public class OfferingService {

    private final OfferingRepository offeringRepository;
    private final ProviderService providerService;

    public OfferingService(OfferingRepository offeringRepository, ProviderService providerService) {
        this.offeringRepository = offeringRepository;
        this.providerService = providerService;
    }

    public List<Offering> getOfferings(Long providerId, OfferingType type) {
        if (providerId != null && type != null) {
            return offeringRepository.findByProviderIdAndType(providerId, type);
        }
        if (providerId != null) {
            return offeringRepository.findByProviderId(providerId);
        }
        if (type != null) {
            return offeringRepository.findByType(type);
        }
        return offeringRepository.findAll();
    }

    public Offering getOffering(Long id) {
        return offeringRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Offering not found"));
    }

    public Offering createOffering(OfferingRequest request) {
        Provider provider = providerService.getProvider(request.providerId());
        validateProviderCapabilities(provider, request.type());

        Offering offering = new Offering();
        copy(request, offering, provider);
        return offeringRepository.save(offering);
    }

    private void validateProviderCapabilities(Provider provider, OfferingType type) {
        if (type == OfferingType.GOODS && !provider.isSupportsGoods()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provider does not support goods");
        }
        if (type == OfferingType.SERVICE && !provider.isSupportsServices()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provider does not support services");
        }
    }

    private void copy(OfferingRequest request, Offering offering, Provider provider) {
        offering.setProvider(provider);
        offering.setName(request.name());
        offering.setDescription(request.description());
        offering.setType(request.type());
        offering.setPrice(request.price());
        offering.setCurrency(request.currency());
        offering.setAvailableUnits(request.availableUnits());
        offering.setDurationMinutes(request.durationMinutes());
        offering.setActive(request.active() == null || request.active());
    }
}

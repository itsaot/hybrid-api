package com.example.hybridapi.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.hybridapi.dto.ProviderRequest;
import com.example.hybridapi.model.BusinessCategory;
import com.example.hybridapi.model.Provider;
import com.example.hybridapi.repository.ProviderRepository;

@Service
public class ProviderService {

    private final ProviderRepository providerRepository;

    public ProviderService(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    public List<Provider> getProviders(BusinessCategory category) {
        if (category != null) {
            return providerRepository.findByCategory(category);
        }
        return providerRepository.findAll();
    }

    public List<Provider> searchProviders(String query) {
        if (query == null || query.isBlank()) {
            return providerRepository.findAll();
        }
        return providerRepository.searchByText(query);
    }

    public Provider getProvider(Long id) {
        return providerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Provider not found"));
    }

    public Provider createProvider(ProviderRequest request) {
        Provider provider = new Provider();
        copy(request, provider);
        return providerRepository.save(provider);
    }

    public Provider updateProvider(Long id, ProviderRequest request) {
        Provider provider = getProvider(id);
        copy(request, provider);
        return providerRepository.save(provider);
    }

    private void copy(ProviderRequest request, Provider provider) {
        provider.setName(request.name());
        provider.setCategory(request.category());
        provider.setCity(request.city());
        provider.setAddressLine(request.addressLine());
        provider.setLatitude(request.latitude());
        provider.setLongitude(request.longitude());
        provider.setDescription(request.description());
        provider.setSupportsGoods(request.supportsGoods());
        provider.setSupportsServices(request.supportsServices());
        provider.setActive(request.active() == null || request.active());
    }
}

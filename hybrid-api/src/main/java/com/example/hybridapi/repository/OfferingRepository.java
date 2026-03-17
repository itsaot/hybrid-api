package com.example.hybridapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hybridapi.model.Offering;
import com.example.hybridapi.model.OfferingType;

public interface OfferingRepository extends JpaRepository<Offering, Long> {
    List<Offering> findByProviderId(Long providerId);
    List<Offering> findByType(OfferingType type);
    List<Offering> findByProviderIdAndType(Long providerId, OfferingType type);
}

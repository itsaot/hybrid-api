package com.example.hybridapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hybridapi.model.BusinessCategory;
import com.example.hybridapi.model.Provider;

public interface ProviderRepository extends JpaRepository<Provider, Long> {
    List<Provider> findByCategory(BusinessCategory category);
}

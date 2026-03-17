package com.example.hybridapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.hybridapi.model.BusinessCategory;
import com.example.hybridapi.model.Provider;

public interface ProviderRepository extends JpaRepository<Provider, Long> {
    List<Provider> findByCategory(BusinessCategory category);

    @Query("""
            select p from Provider p
            where lower(p.name) like lower(concat('%', :query, '%'))
               or lower(p.city) like lower(concat('%', :query, '%'))
               or lower(coalesce(p.addressLine, '')) like lower(concat('%', :query, '%'))
            """)
    List<Provider> searchByText(@Param("query") String query);
}

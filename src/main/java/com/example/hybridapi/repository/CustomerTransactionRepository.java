package com.example.hybridapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hybridapi.model.CustomerTransaction;

public interface CustomerTransactionRepository extends JpaRepository<CustomerTransaction, Long> {
}

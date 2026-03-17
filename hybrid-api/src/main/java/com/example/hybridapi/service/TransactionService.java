package com.example.hybridapi.service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.hybridapi.dto.CreateTransactionRequest;
import com.example.hybridapi.dto.TransactionLineRequest;
import com.example.hybridapi.model.CustomerTransaction;
import com.example.hybridapi.model.Offering;
import com.example.hybridapi.model.OfferingType;
import com.example.hybridapi.model.TransactionLine;
import com.example.hybridapi.model.TransactionType;
import com.example.hybridapi.repository.CustomerTransactionRepository;
import com.example.hybridapi.repository.OfferingRepository;

@Service
public class TransactionService {

    private final CustomerTransactionRepository transactionRepository;
    private final OfferingService offeringService;
    private final OfferingRepository offeringRepository;

    public TransactionService(
            CustomerTransactionRepository transactionRepository,
            OfferingService offeringService,
            OfferingRepository offeringRepository
    ) {
        this.transactionRepository = transactionRepository;
        this.offeringService = offeringService;
        this.offeringRepository = offeringRepository;
    }

    public List<CustomerTransaction> getTransactions() {
        return transactionRepository.findAll();
    }

    public CustomerTransaction getTransaction(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));
    }

    @Transactional
    public CustomerTransaction createTransaction(CreateTransactionRequest request) {
        CustomerTransaction transaction = new CustomerTransaction();
        transaction.setCustomerName(request.customerName());
        transaction.setCustomerEmail(request.customerEmail());
        transaction.setCreatedAt(OffsetDateTime.now());

        BigDecimal totalAmount = BigDecimal.ZERO;
        boolean hasGoods = false;
        boolean hasServices = false;

        for (TransactionLineRequest lineRequest : request.lines()) {
            Offering offering = offeringService.getOffering(lineRequest.offeringId());

            if (!offering.isActive()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Offering is inactive: " + offering.getName());
            }
            if (offering.getAvailableUnits() < lineRequest.quantity()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient availability for: " + offering.getName());
            }
            if (offering.getType() == OfferingType.SERVICE && lineRequest.serviceDate() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Service date is required for: " + offering.getName());
            }

            offering.setAvailableUnits(offering.getAvailableUnits() - lineRequest.quantity());
            offeringRepository.save(offering);

            TransactionLine line = new TransactionLine();
            line.setOffering(offering);
            line.setQuantity(lineRequest.quantity());
            line.setUnitPrice(offering.getPrice());
            line.setServiceDate(lineRequest.serviceDate());
            line.setNotes(lineRequest.notes());
            transaction.addLine(line);

            BigDecimal lineTotal = offering.getPrice().multiply(BigDecimal.valueOf(lineRequest.quantity()));
            totalAmount = totalAmount.add(lineTotal);

            hasGoods = hasGoods || offering.getType() == OfferingType.GOODS;
            hasServices = hasServices || offering.getType() == OfferingType.SERVICE;
        }

        transaction.setTotalAmount(totalAmount);
        transaction.setTransactionType(resolveType(hasGoods, hasServices));
        return transactionRepository.save(transaction);
    }

    private TransactionType resolveType(boolean hasGoods, boolean hasServices) {
        if (hasGoods && hasServices) {
            return TransactionType.HYBRID;
        }
        if (hasGoods) {
            return TransactionType.GOODS_ONLY;
        }
        return TransactionType.SERVICE_ONLY;
    }
}

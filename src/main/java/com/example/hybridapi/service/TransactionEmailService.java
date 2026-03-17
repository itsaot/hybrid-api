package com.example.hybridapi.service;

import java.math.BigDecimal;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.example.hybridapi.config.EmailProperties;
import com.example.hybridapi.dto.EmailSendResponse;
import com.example.hybridapi.dto.EmailTestRequest;
import com.example.hybridapi.model.CustomerTransaction;
import com.example.hybridapi.model.TransactionLine;

@Service
public class TransactionEmailService {

    private static final Logger log = LoggerFactory.getLogger(TransactionEmailService.class);

    private final EmailService emailService;
    private final EmailProperties emailProperties;

    public TransactionEmailService(EmailService emailService, EmailProperties emailProperties) {
        this.emailService = emailService;
        this.emailProperties = emailProperties;
    }

    public void sendConfirmationIfConfigured(CustomerTransaction transaction) {
        if (!emailProperties.isConfigured()) {
            return;
        }

        try {
            EmailSendResponse ignored = emailService.sendTestEmail(new EmailTestRequest(
                    transaction.getCustomerEmail(),
                    "Your hybrid booking confirmation #" + transaction.getId(),
                    buildHtml(transaction)
            ));
        } catch (RuntimeException exception) {
            log.warn("Transaction {} succeeded but confirmation email could not be sent: {}", transaction.getId(), exception.getMessage());
        }
    }

    private String buildHtml(CustomerTransaction transaction) {
        String lines = transaction.getLines().stream()
                .map(this::formatLine)
                .collect(Collectors.joining());

        return """
                <h2>Booking confirmed</h2>
                <p>Hello %s,</p>
                <p>Your transaction has been confirmed.</p>
                <p><strong>Reference:</strong> #%d</p>
                <p><strong>Type:</strong> %s</p>
                <ul>%s</ul>
                <p><strong>Total:</strong> %s</p>
                """.formatted(
                transaction.getCustomerName(),
                transaction.getId(),
                transaction.getTransactionType(),
                lines,
                formatAmount(transaction.getTotalAmount())
        );
    }

    private String formatLine(TransactionLine line) {
        String serviceDate = line.getServiceDate() != null ? " on " + line.getServiceDate() : "";
        return "<li>%s x%d%s</li>".formatted(
                line.getOffering().getName(),
                line.getQuantity(),
                serviceDate
        );
    }

    private String formatAmount(BigDecimal amount) {
        return amount == null ? "0.00" : amount.toPlainString();
    }
}

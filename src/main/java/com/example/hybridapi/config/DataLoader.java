package com.example.hybridapi.config;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.hybridapi.model.BusinessCategory;
import com.example.hybridapi.model.Offering;
import com.example.hybridapi.model.OfferingType;
import com.example.hybridapi.model.Provider;
import com.example.hybridapi.repository.OfferingRepository;
import com.example.hybridapi.repository.ProviderRepository;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner seedData(ProviderRepository providerRepository, OfferingRepository offeringRepository) {
        return args -> {
            if (providerRepository.count() > 0) {
                return;
            }

            Provider hospital = createProvider(
                    "CityCare Hospital",
                    BusinessCategory.HOSPITAL,
                    "Johannesburg",
                    "12 Wellness Avenue, Rosebank",
                    -26.145300,
                    28.041300,
                    "Hospital with consultation services and a pharmacy.",
                    true,
                    true
            );
            Provider hotel = createProvider(
                    "Sunset Grand Hotel",
                    BusinessCategory.HOTEL,
                    "Cape Town",
                    "88 Beach Road, Sea Point",
                    -33.912200,
                    18.389700,
                    "Hotel with room bookings, airport pickup, and gift shop items.",
                    true,
                    true
            );
            Provider rentals = createProvider(
                    "DriveNow Rentals",
                    BusinessCategory.CAR_RENTAL,
                    "Durban",
                    "5 Lagoon Drive, Umhlanga",
                    -29.726700,
                    31.084700,
                    "Car hire with optional accessories and support services.",
                    true,
                    true
            );

            hospital = providerRepository.save(hospital);
            hotel = providerRepository.save(hotel);
            rentals = providerRepository.save(rentals);

            offeringRepository.save(createOffering(hospital, "General Consultation", OfferingType.SERVICE, "Doctor appointment", "USD", "30", 20, 30));
            offeringRepository.save(createOffering(hospital, "Pain Relief Pack", OfferingType.GOODS, "Over-the-counter care pack", "USD", "15", 120, 0));
            offeringRepository.save(createOffering(hotel, "Deluxe Room Night", OfferingType.SERVICE, "One night accommodation", "USD", "145", 18, 1440));
            offeringRepository.save(createOffering(hotel, "Welcome Gift Basket", OfferingType.GOODS, "In-room celebration basket", "USD", "35", 50, 0));
            offeringRepository.save(createOffering(rentals, "SUV Daily Rental", OfferingType.SERVICE, "One day vehicle rental", "USD", "80", 12, 1440));
            offeringRepository.save(createOffering(rentals, "Child Seat", OfferingType.GOODS, "Safety seat add-on", "USD", "12", 25, 0));
        };
    }

    private Provider createProvider(
            String name,
            BusinessCategory category,
            String city,
            String addressLine,
            double latitude,
            double longitude,
            String description,
            boolean supportsGoods,
            boolean supportsServices
    ) {
        Provider provider = new Provider();
        provider.setName(name);
        provider.setCategory(category);
        provider.setCity(city);
        provider.setAddressLine(addressLine);
        provider.setLatitude(latitude);
        provider.setLongitude(longitude);
        provider.setDescription(description);
        provider.setSupportsGoods(supportsGoods);
        provider.setSupportsServices(supportsServices);
        provider.setActive(true);
        return provider;
    }

    private Offering createOffering(
            Provider provider,
            String name,
            OfferingType type,
            String description,
            String currency,
            String price,
            int availableUnits,
            int durationMinutes
    ) {
        Offering offering = new Offering();
        offering.setProvider(provider);
        offering.setName(name);
        offering.setType(type);
        offering.setDescription(description);
        offering.setCurrency(currency);
        offering.setPrice(new BigDecimal(price));
        offering.setAvailableUnits(availableUnits);
        offering.setDurationMinutes(durationMinutes);
        offering.setActive(true);
        return offering;
    }
}

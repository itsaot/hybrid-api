package com.example.hybridapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class HybridApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(HybridApiApplication.class, args);
    }
}

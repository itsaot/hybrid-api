package com.example.hybridapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI hybridApiOpenAPI() {
        String schemeName = "basicAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Hybrid Business API")
                        .description("REST API for businesses that provide services, goods, or both.")
                        .version("v1")
                        .contact(new Contact().name("Hybrid API Team")))
                .addSecurityItem(new SecurityRequirement().addList(schemeName))
                .schemaRequirement(schemeName, new SecurityScheme()
                        .name(schemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("basic"));
    }
}

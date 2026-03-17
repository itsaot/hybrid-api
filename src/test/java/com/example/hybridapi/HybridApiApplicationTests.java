package com.example.hybridapi;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class HybridApiApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    void contextLoadsAndReturnsSeededProviders() throws Exception {
        mockMvc.perform(get("/api/providers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].supportsServices").exists());
    }

    @Test
    void providersEndpointRequiresAuthentication() throws Exception {
        mockMvc.perform(get("/api/providers"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void paymentConfigEndpointReturnsConfigurationShape() throws Exception {
        mockMvc.perform(get("/api/payments/config"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.configured").exists())
                .andExpect(jsonPath("$.secretKeyPreview").exists());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void emailConfigEndpointReturnsConfigurationShape() throws Exception {
        mockMvc.perform(get("/api/email/config"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.provider").exists())
                .andExpect(jsonPath("$.configured").exists());
    }

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    void weatherConfigEndpointReturnsConfigurationShape() throws Exception {
        mockMvc.perform(get("/api/integrations/weather/config"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.provider").exists())
                .andExpect(jsonPath("$.enabled").exists());
    }

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    void mapsConfigEndpointReturnsConfigurationShape() throws Exception {
        mockMvc.perform(get("/api/integrations/maps/config"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.provider").exists())
                .andExpect(jsonPath("$.tilesBaseUrl").exists());
    }

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    void providerSearchReturnsResults() throws Exception {
        mockMvc.perform(get("/api/providers/search").param("query", "Rosebank"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].city").exists());
    }

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    void mapProviderMarkersReturnCoordinates() throws Exception {
        mockMvc.perform(get("/api/integrations/maps/providers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].latitude").exists())
                .andExpect(jsonPath("$[0].longitude").exists());
    }
}

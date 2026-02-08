package csembstu.alamgir.server.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import csembstu.alamgir.server.dto.request.DateRequestValidation;
import csembstu.alamgir.server.dto.response.*;
import csembstu.alamgir.server.service.ValidationApiService2;

@WebMvcTest(ValidationApiController2.class)
class ValidationApiController2Test {

        @Autowired  private MockMvc mockMvc;
        @Autowired  private ObjectMapper objectMapper;

        @MockitoBean
        private ValidationApiService2 validationApiService2;

        private DateRequestValidation request;
        private FullLogisticsValidationResponse response;



        @BeforeEach
        void init() {

                request = new DateRequestValidation().setDate("2026-02-08");
                response = new FullLogisticsValidationResponse()
                                .setTemperature_incompatible_demands(List.of("Product Ice Cream has no safe warehouse"))
                                .setCapacity_violations(
                                                List.of(new CapacityViolation(
                                                                "MAX_CAPACITY_VIOLATION",
                                                                "Warehouse A",
                                                                "Shop",
                                                                10,
                                                                100)))
                                .setStorage_capacity_violations(
                                                List.of(new StorageCapacityViolation(
                                                                "Warehouse A",
                                                                300,
                                                                200)));
        }



        @Test
        @DisplayName("POST /full-logistics-validation should return validation result")
        void fullLogisticsValidation_success() throws Exception {

                // GIVEN
                when(validationApiService2.fullLogisticsValidation(any(DateRequestValidation.class))).thenReturn(response);

                // WHEN & THEN
                mockMvc.perform(post("/full-logistics-validation")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.temperature_incompatible_demands").isArray())
                                .andExpect(jsonPath("$.temperature_incompatible_demands[0]").exists())
                                .andExpect(jsonPath("$.capacity_violations").isArray())
                                .andExpect(jsonPath("$.storage_capacity_violations").isArray());
        }
}

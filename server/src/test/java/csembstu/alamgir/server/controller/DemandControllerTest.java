package csembstu.alamgir.server.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static java.util.Arrays.asList;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import csembstu.alamgir.server.dto.LocationType;
import csembstu.alamgir.server.dto.request.CreateDemandRequest;
import csembstu.alamgir.server.dto.response.DemandResponse;
import csembstu.alamgir.server.entity.Location;
import csembstu.alamgir.server.entity.Product;
import csembstu.alamgir.server.service.DemandService;

@WebMvcTest(DemandController.class)
public class DemandControllerTest {

    @Autowired  private MockMvc mockMvc;
    @Autowired  private ObjectMapper objectMapper;

    @MockitoBean  DemandService demandService;

    private Location mockLocation;
    private Product mockProduct;
    private CreateDemandRequest mockDemandRequest;
    private DemandResponse demandResponse;


    @BeforeEach
    void init() {
        mockLocation = new Location() .setName("FreshMart Uttara").setType(LocationType.RETAILER) .setCity("Bogura");
        mockProduct = new Product().setName("Fresh Milk").setMaxTemperature(2.0).setMinTemperature(6.0);
        mockDemandRequest = new CreateDemandRequest().setDate("2026-02-07").setMinQuantity(60).setMaxQuantity(120);
        demandResponse = new DemandResponse().setDate("2026-02-07").setMinQuantity(60).setMaxQuantity(120);
    }



    @Test
    @DisplayName("POST /api/demand : 201 CREATED")
    void shouldCreateDemandSuccessfully() throws Exception {

        // GIVEN
        String locationId = "L1";
        String productId = "P1";

        mockDemandRequest.setLocationId(locationId).setProductId(productId);
        demandResponse.setId("D1") .setLocationId(locationId) .setProductId(productId);

        Mockito.when(demandService.createDemand(Mockito.any(CreateDemandRequest.class))).thenReturn(demandResponse);

        // WHEN & THEN
        mockMvc.perform(post("/api/demands")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockDemandRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("D1"))
                .andExpect(jsonPath("$.locationId").value("L1"))
                .andExpect(jsonPath("$.productId").value("P1"));

       //VERIFY         
        Mockito.verify(demandService).createDemand(Mockito.any(CreateDemandRequest.class));
    }




    @Test
    @DisplayName("GET /api/demand : 201 CREATED")
    void shouldReturnDemandAllDemand() throws Exception {

        // GIVEN
        String locationId = "L1";
        String productId = "P1";

        mockDemandRequest.setLocationId(locationId).setProductId(productId);
        demandResponse.setId("D1").setLocationId(locationId).setProductId(productId);

        // WHEN AND THEN
        Mockito.when(demandService.getAllDemand()).thenReturn(asList(demandResponse));
        mockMvc.perform(get("/api/demands"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value("D1"))
                .andExpect(jsonPath("$[0].locationId").value("L1"))
                .andExpect(jsonPath("$[0].productId").value("P1"));

        // VERIFY
        Mockito.verify(demandService).getAllDemand();
    }

}

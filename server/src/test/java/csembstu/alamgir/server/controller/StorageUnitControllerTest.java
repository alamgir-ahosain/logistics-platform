package csembstu.alamgir.server.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

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

import csembstu.alamgir.server.dto.request.CreateStorageUnitRequest;
import csembstu.alamgir.server.dto.response.StorageUnitResponse;
import csembstu.alamgir.server.service.StorageUnitService;


@WebMvcTest(StorageUnitController.class)
public class StorageUnitControllerTest {

    @Autowired   private MockMvc mockMvc;
    @Autowired   private ObjectMapper objectMapper;
    
    @MockitoBean   private StorageUnitService storageUnitService;


    private CreateStorageUnitRequest mockStorageUnit1;
    private CreateStorageUnitRequest mockStorageUnit2;



        @BeforeEach
        void init() {
                mockStorageUnit1 = new CreateStorageUnitRequest().setMinTemperature(-10.0).setMaxTemperature(-5.0).setCapacity(500);
                mockStorageUnit2 = new CreateStorageUnitRequest().setMinTemperature(0.0).setMaxTemperature(10.0).setCapacity(300);
        }



        private StorageUnitResponse mapToEntity(CreateStorageUnitRequest request, String id) {
                StorageUnitResponse unit = new StorageUnitResponse();
                unit.setId(id).setLocationId(request.getLocationId()).setMinTemperature(request.getMinTemperature()).setMaxTemperature(request.getMaxTemperature()).setCapacity(request.getCapacity());
                return unit;
        }




    @Test
    @DisplayName("POST /api/storage-units : 201 CREATED")
    void shouldCreateStorageUnitSuccessfully() throws Exception {

        // GIVEN
        mockStorageUnit1.setLocationId("loc-1");
        StorageUnitResponse unit = mapToEntity(mockStorageUnit1, "w1");
        Mockito.when(storageUnitService.createStorageUnit(Mockito.any(CreateStorageUnitRequest.class))).thenReturn(unit);

        // WHEN & THEN
        mockMvc.perform(post("/api/storage-units")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockStorageUnit1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("w1"))
                .andExpect(jsonPath("$.minTemperature").value(-10.0))
                .andExpect(jsonPath("$.maxTemperature").value(-5.0))
                .andExpect(jsonPath("$.capacity").value(500));

        Mockito.verify(storageUnitService).createStorageUnit(Mockito.any(CreateStorageUnitRequest.class));
    }




    @Test
    @DisplayName("GET /api/storage-units :  200 OK")
    void shouldReturnAllLocation() throws Exception {

        // GIVEN
        mockStorageUnit1.setLocationId("loc-1");
        mockStorageUnit2.setLocationId("loc-2");

        StorageUnitResponse savedResponse1 = mapToEntity(mockStorageUnit1, "w1");
        StorageUnitResponse savedResponse2 = mapToEntity(mockStorageUnit2, "w2");
        List<StorageUnitResponse> responses = Arrays.asList(savedResponse1, savedResponse2);

        // WHEN AND THEN
        Mockito.when(storageUnitService.getAllStorageUnit()).thenReturn(responses);
        mockMvc.perform(get("/api/storage-units"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].capacity").value(500))
                .andExpect(jsonPath("$[1].capacity").value(300));

        // VERIFY
        Mockito.verify(storageUnitService).getAllStorageUnit();

    }

}

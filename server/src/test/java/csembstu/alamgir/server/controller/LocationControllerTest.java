package csembstu.alamgir.server.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

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
import csembstu.alamgir.server.dto.request.CreateLocationRequest;
import csembstu.alamgir.server.entity.Location;
import csembstu.alamgir.server.service.LocationService;

@WebMvcTest(LocationController.class)
public class LocationControllerTest {

    @Autowired       private MockMvc mockMvc;
    @MockitoBean  private LocationService locationService;
    @Autowired       private ObjectMapper objectMapper;

    private CreateLocationRequest newLocation1;
    private CreateLocationRequest newLocation2;

    @BeforeEach
    void init() {
        newLocation1 = new CreateLocationRequest(); newLocation1.setName("Farm Alpha");  newLocation1.setType(LocationType.PRODUCER);  newLocation1.setCity("Bogura");
        newLocation2 = new CreateLocationRequest(); newLocation2.setName("Central Cold Warehouse"); newLocation2.setType(LocationType.WAREHOUSE); newLocation2.setCity("Dhaka");
    }




    private Location mapToEntity(CreateLocationRequest request, String id) {
        Location location = new Location(); location.setId(id);  location.setName(request.getName());  location.setType(request.getType());  location.setCity(request.getCity());
        return location;
    }




    @Test
    @DisplayName("POST /api/location :  201 CREATED")

    void shouldCreateLocationSuccessfully() throws Exception {

        // GIVEN
        Location savedLocation = mapToEntity(newLocation1, "p1");
        Mockito.when(locationService.createLocation(Mockito.any(CreateLocationRequest.class))).thenReturn(savedLocation);

        // WHEN AND THEN
        mockMvc.perform(post("/api/location").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newLocation1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("p1"))
                .andExpect(jsonPath("$.name").value("Farm Alpha"))
                .andExpect(jsonPath("$.city").value("Bogura"));

        // VERIFY
        Mockito.verify(locationService).createLocation(Mockito.any(CreateLocationRequest.class));
    }




    @Test
    @DisplayName("GET /api/location :  200 OK")
    void shouldReturnAllLocation() throws Exception {

        // GIVEN
        Location savedLocation = mapToEntity(newLocation1, "p1");
        Location savedLocation2 = mapToEntity(newLocation2, "w1");
        List<Location> locationList = Arrays.asList(savedLocation, savedLocation2);

        // WHEN AND THEN
        Mockito.when(locationService.getAllLocations()).thenReturn(locationList);

        mockMvc.perform(get("/api/location"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Farm Alpha"))
                .andExpect(jsonPath("$[0].type").value("PRODUCER"))
                .andExpect(jsonPath("$[1].name").value("Central Cold Warehouse"))
                .andExpect(jsonPath("$.size()").value(locationList.size()));

        // VERIFY
        Mockito.verify(locationService).getAllLocations();
    }
}

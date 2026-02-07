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

import csembstu.alamgir.server.dto.LocationType;
import csembstu.alamgir.server.dto.request.CreateRouteRequest;
import csembstu.alamgir.server.dto.response.RouteResponse;
import csembstu.alamgir.server.entity.Location;
import csembstu.alamgir.server.service.RouteService;

@WebMvcTest(RouteController.class)
public class RouteControllerTest {

    @Autowired  private MockMvc mockMvc;
    @Autowired  private ObjectMapper objectMapper;

    @MockitoBean
    private RouteService routeService;
    
    private Location mockLocation1;
    private Location mockLocation2;

    private CreateRouteRequest routeRequest;
    private RouteResponse routeResponse;



    @BeforeEach
    void init() {
        mockLocation1 = new Location().setName("Central Cold Warehouse").setType(LocationType.WAREHOUSE).setCity("Dhaka");
        mockLocation2 = new Location().setName("FreshMart Uttara").setType(LocationType.RETAILER).setCity("Dhaka");

        routeRequest = new CreateRouteRequest().setCapacity(250).setMinShipment(40);
        routeResponse = new RouteResponse().setCapacity(250).setMinShipment(40);
    }



    @Test
    @DisplayName("POST /api/routes : 201 CREATED")
    void shouldCreateRouteSuccessfully() throws Exception {

        // GIVEN
        String fromId = "w1";
        String toId = "R1";

        mockLocation1.setId(fromId);
        mockLocation2.setId(toId);

        routeRequest.setFromLocationId(fromId).setToLocationId(toId);
        routeResponse.setId("RT1").setFromLocationId(fromId).setToLocationId(toId);

        Mockito.when(routeService.createRoute(Mockito.any(CreateRouteRequest.class))).thenReturn(routeResponse);

        // WHEN & THEN
        mockMvc.perform(post("/api/routes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(routeRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("RT1"))
                .andExpect(jsonPath("$.capacity").value(250))
                .andExpect(jsonPath("$.minShipment").value(40));

        Mockito.verify(routeService).createRoute(Mockito.any(CreateRouteRequest.class));
    }




    @Test
    @DisplayName("GET /api/routes :  200 OK")
    void shouldReturnAllRoute() throws Exception {

        // GIVEN
        String fromId = "w1";
        String toId = "R1";

        mockLocation1.setId(fromId);
        mockLocation2.setId(toId);

        routeResponse.setId("RT1").setFromLocationId(fromId).setToLocationId(toId);

        List<RouteResponse> responses = Arrays.asList(routeResponse);

        // WHEN AND THEN
        Mockito.when(routeService.getAllRoute()).thenReturn(responses);
        mockMvc.perform(get("/api/routes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].capacity").value(250))
                .andExpect(jsonPath("$[0].minShipment").value(40));

        // VERIFY
        Mockito.verify(routeService).getAllRoute();
    }

}

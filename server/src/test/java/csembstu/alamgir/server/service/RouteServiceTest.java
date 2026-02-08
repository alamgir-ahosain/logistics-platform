package csembstu.alamgir.server.service;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import csembstu.alamgir.server.dto.LocationType;
import csembstu.alamgir.server.dto.request.CreateRouteRequest;
import csembstu.alamgir.server.dto.response.RouteResponse;
import csembstu.alamgir.server.entity.Location;
import csembstu.alamgir.server.entity.Route;
import csembstu.alamgir.server.exception.BadRequestException;
import csembstu.alamgir.server.exception.NotFoundException;
import csembstu.alamgir.server.repository.LocationRepository;
import csembstu.alamgir.server.repository.RouteRepository;

@ExtendWith(MockitoExtension.class)
public class RouteServiceTest {

    @Mock
    private RouteRepository routeRepository;

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private RouteService routeService;

    private CreateRouteRequest routeRequest;
    private RouteResponse routeResponse;
    private Route route;

    private Location mockLocation1;
    private Location mockLocation2;


    @BeforeEach
    void init() {
        mockLocation1 = new Location().setName("Central Cold Warehouse").setType(LocationType.WAREHOUSE).setCity("Dhaka");
        mockLocation2 = new Location().setName("FreshMart Uttara").setType(LocationType.RETAILER).setCity("Dhaka");

        routeRequest = new CreateRouteRequest().setCapacity(250).setMinShipment(40);
        routeResponse = new RouteResponse().setCapacity(250).setMinShipment(40);

        route = new Route().setCapacity(250).setMinShipment(40);
    }


    @Test
    @DisplayName("Create StorageUnit: Should save successfully when Location is exist")
    void shouldSaveRoutetSuccessfullyWhenLocationExist() {

        // GIVEN
        String fromId = "w1";
        String toId = "R1";

        mockLocation1.setId(fromId);
        mockLocation2.setId(toId);
        routeRequest.setFromLocationId(fromId).setToLocationId(toId);

        when(locationRepository.findById(fromId)).thenReturn(Optional.of(mockLocation1));
        when(locationRepository.findById(toId)).thenReturn(Optional.of(mockLocation2));

        // WHEN 
        RouteResponse response = routeService.createRoute(routeRequest);

        // Assert
        assertNotNull(response);
        assertEquals(fromId, response.getFromLocationId());
        assertEquals(toId, response.getToLocationId());

        // Verify
        verify(routeRepository, times(1)).save(any(Route.class));
    }




    @Test
    @DisplayName("Should throw NotFoundException when FromLocation does not exist")
    void shouldThrowNotFoundExceptionWhenLocationDoesNotExist() {

        // GIVEN
        String fromId = "w1";
        String toId = "R1";
        mockLocation1.setId(fromId);
        mockLocation2.setId(toId);

        String nonExistentFromLocationId = "Alamgir";
        routeRequest.setFromLocationId(nonExistentFromLocationId).setToLocationId(toId);
        when(locationRepository.findById(nonExistentFromLocationId)).thenReturn(Optional.empty());

        // WHEN
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            routeService.createRoute(routeRequest);
        });

        //THEN
        assertEquals("Location does not exist: " + nonExistentFromLocationId, exception.getMessage());

        // Verify
        verify(routeRepository, never()).save(any(Route.class));
    }



    @Test
    @DisplayName("Should throw NotFoundException when ToLocation does not exist")
    void shouldThrowNotFoundExceptionWhenToLocationDoesNotExist() {

        // GIVEN
        String fromId = "w1";
        String toId = "R1";
        mockLocation1.setId(fromId);
        mockLocation2.setId(toId);

        String nonExistentToLocationId = "Alamgir";

        routeRequest.setFromLocationId(fromId).setToLocationId(nonExistentToLocationId);

        when(locationRepository.findById(fromId)).thenReturn(Optional.of(mockLocation1));
        when(locationRepository.findById(nonExistentToLocationId)).thenReturn(Optional.empty());

        //WHEN AND THEN
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            routeService.createRoute(routeRequest);
        });

        assertEquals("Location does not exist: " + nonExistentToLocationId, exception.getMessage());

        // Verify
        verify(routeRepository, never()).save(any(Route.class));
    }




    @Test
    @DisplayName("Should throw BadRequestException for invalid  ROUTE")
    void shouldThrowExceptionForInvalidRouteType() {

        //GIVEN
        mockLocation1.setType(LocationType.RETAILER).setId("R1");
        mockLocation2.setType(LocationType.PRODUCER).setId("P1");
        routeRequest.setFromLocationId("R1").setToLocationId("P1");

        //WHEN
        when(locationRepository.findById("R1")).thenReturn(Optional.of(mockLocation1));
        when(locationRepository.findById("P1")).thenReturn(Optional.of(mockLocation2));

        // THEN
        assertThrows(BadRequestException.class, () -> routeService.createRoute(routeRequest));
    }



    @Test
    @DisplayName("Get All StorageUnit: Should return list of all Route")
    void shouldReturnAllRouteSuccessfully() {

        // GIVEN
        String fromId = "w1";
        String toId = "R1";
        mockLocation1.setId(fromId);
        mockLocation2.setId(toId);

        routeResponse.setFromLocationId(fromId).setToLocationId(toId);

        route.setFromLocation(mockLocation1).setToLocation(mockLocation2);
        when(routeRepository.findAll()).thenReturn(asList(route));

        //WHEN
        List<RouteResponse> responses = routeService.getAllRoute();

        // THEN
        assertNotNull(responses);
        assertEquals(1, responses.size());

        // VERIFY
        verify(routeRepository, times(1)).findAll();
    }

}

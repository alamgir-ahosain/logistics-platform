package csembstu.alamgir.server.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import csembstu.alamgir.server.dto.LocationType;
import csembstu.alamgir.server.entity.Location;
import csembstu.alamgir.server.entity.Route;

@DataJpaTest
public class RouteRepositoryTest {

    @Autowired  LocationRepository locationRepository;
    @Autowired  RouteRepository routeRepository;

    private Route mockroute;
    private Location mockLocation1;
    private Location mockLocation2;


    @BeforeEach
    void init() {
        mockLocation1 = new Location().setName("Central Cold Warehouse").setType(LocationType.WAREHOUSE).setCity("Dhaka");
        mockLocation2 = new Location().setName("FreshMart Uttara").setType(LocationType.RETAILER).setCity("Dhaka");

        mockroute = new Route().setFromLocation(mockLocation1).setToLocation(mockLocation2).setCapacity(300).setMinShipment(50);
    }



    @Test
    @DisplayName("Should return Route matching from_location_id")
    void shouldReturnRouteThatMatchFromLocationId() {
        
        // GIVEN
        Location savedSource = locationRepository.save(mockLocation1);
        locationRepository.save(mockLocation2);
        routeRepository.save(mockroute);

        // WHEN 
        List<Route> routes = routeRepository.findByFromLocation_Id(savedSource.getId());

        // Assert
        assertNotNull(routes);
        assertEquals(1, routes.size());
        assertEquals("Central Cold Warehouse", routes.get(0).getFromLocation().getName());
    }


    @Test
    @DisplayName("Should return Route matching to_location_id")
    void shouldReturnRouteThatMatchToLocationId() {
        
        // GIVEN
        locationRepository.save(mockLocation1);
        Location savedSource = locationRepository.save(mockLocation2);
        routeRepository.save(mockroute);

        // WHEN
        List<Route> routes = routeRepository.findByToLocation_Id(savedSource.getId());

        // Assert
        assertNotNull(routes);
        assertEquals(1, routes.size());
        assertEquals("FreshMart Uttara", routes.get(0).getToLocation().getName());
    }

}

package csembstu.alamgir.server.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import csembstu.alamgir.server.dto.LocationType;
import csembstu.alamgir.server.dto.request.CreateLocationRequest;
import csembstu.alamgir.server.entity.Location;
import csembstu.alamgir.server.repository.LocationRepository;

@ExtendWith(MockitoExtension.class)
public class LocationServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private LocationService locationService;

    // newLocationRequest1
    private CreateLocationRequest newLocation1;

    private
    CreateLocationRequest newLocation2;

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
    @DisplayName("Create Location: Should save successfully when location is unique")
    void shouldSaveLocationSuccessfullyWhenUnique() throws BadRequestException {

        // GIVEN
        Location mockedSavedLocation = mapToEntity(newLocation1, "p1");

        when(locationRepository.existsByNameAndCity(newLocation1.getName(), newLocation1.getCity())).thenReturn(false);
        when(locationRepository.save(any(Location.class))).thenReturn(mockedSavedLocation);

        // WHEN
        Location savedLocation = locationService.createLocation(newLocation1);

        // THEN
        assertNotNull(savedLocation);
        assertEquals("Farm Alpha", savedLocation.getName());
        assertEquals(LocationType.PRODUCER, savedLocation.getType());
        assertEquals("Bogura", savedLocation.getCity());

        // VERIFY
        verify(locationRepository).existsByNameAndCity("Farm Alpha", "Bogura");
        verify(locationRepository, times(1)).save(any(Location.class));
    }




    @Test
    @DisplayName("Create Location: Should throw BadRequestException when location exists")
    void shouldThrowExceptionWhenLocationAlreadyExists() {

        // GIVEN
        Location mockedSavedLocation = mapToEntity(newLocation1, "p1");
        when(locationRepository.existsByNameAndCity(mockedSavedLocation.getName(), mockedSavedLocation.getCity())).thenReturn(true);

        // WHEN
        BadRequestException exception = assertThrows(BadRequestException.class, () -> { locationService.createLocation(newLocation1);});

        // THEN
        assertTrue(exception.getMessage().contains("already exists"));

        // VERIFY
        verify(locationRepository, never()).save(any(Location.class));
    }

    @Test
    @DisplayName("Get All Locations: Should return list of all locations")
    void shouldReturnAllLocationsSuccessfully() {

        // GIVEN
        Location savedLocation = mapToEntity(newLocation1, "p1");
        Location savedLocation2 = mapToEntity(newLocation2, "w1");
        List<Location> locationList = Arrays.asList(savedLocation, savedLocation2);

        when(locationRepository.findAll()).thenReturn(locationList);

        // WHEN
        List<Location> locations = locationService.getAllLocations();

        // THEN
        assertNotNull(locations);
        assertEquals(2, locations.size());
        
        // VERIFY
        verify(locationRepository, times(1)).findAll();

    }
}

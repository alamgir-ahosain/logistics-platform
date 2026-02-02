package csembstu.alamgir.server.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import csembstu.alamgir.server.dto.LocationType;
import csembstu.alamgir.server.entity.Location;

@DataJpaTest
public class LocationRepositoryTest {

    @Autowired  private LocationRepository locationRepository;

    @BeforeEach
    void init() {
        Location mockLocation = new Location(); mockLocation.setName("Farm Alpha"); mockLocation.setType(LocationType.PRODUCER); mockLocation.setCity("Bogura");
        locationRepository.save(mockLocation);
    }


    @Test
    @DisplayName("EXISTS: Should return true when both name and city match")
    void shouldReturnTrueWhenNameAndCityMatch() {
        boolean exists = locationRepository.existsByNameAndCity("Farm Alpha", "Bogura");
        assertTrue(exists);
    }


    @Test
    @DisplayName("EXISTS: Should return false when city does not match")
    void shouldReturnFalseWhenCityIsIncorrect() {
        boolean exists = locationRepository.existsByNameAndCity("Farm Alpha", "Dhaka");
        assertFalse(exists);
    }


    @Test
    @DisplayName("Verify saved location fields are correct")
    void shouldVerifyStoredData() {
       
        Location saved = locationRepository.findAll().get(0);

        assertEquals("Farm Alpha", saved.getName());
        assertEquals(LocationType.PRODUCER, saved.getType());
        assertEquals("Bogura", saved.getCity());
    }
}
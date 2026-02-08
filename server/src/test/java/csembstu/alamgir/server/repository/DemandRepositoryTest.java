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
import csembstu.alamgir.server.entity.Demand;
import csembstu.alamgir.server.entity.Location;
import csembstu.alamgir.server.entity.Product;

@DataJpaTest
public class DemandRepositoryTest {

    @Autowired   private LocationRepository locationRepository;
    @Autowired   ProductRepository productRepository;
    @Autowired   private DemandRepository demandRepository;

    private Location mockLocation1;
    private Product mockProduct1;
    private Demand mockDemand;

    @BeforeEach
    void init() {

        mockLocation1 = new Location()
                .setName("Farm Alpha")
                .setType(LocationType.PRODUCER)
                .setCity("Bogura");

        mockProduct1 = new Product()
                .setName("Frozen Vaccine")
                .setMaxTemperature(-20)
                .setMinTemperature(-10);

        mockDemand = new Demand().setDate("2026-02-07")
                .setMinQuantity(60)
                .setMaxQuantity(120);
    }


    @Test
    @DisplayName("Should return Demand that matching the  date")
    void shouldReturnDemandThatMatchDate() {

        // GIVEN
        locationRepository.save(mockLocation1);
        productRepository.save(mockProduct1);
        
        mockDemand.setLocation(mockLocation1).setProduct(mockProduct1);
        demandRepository.save(mockDemand);
        
        // WHEN
        List<Demand> demands = demandRepository.findByDate(mockDemand.getDate());

        // THEN
        assertNotNull(demands);
        assertEquals(1, demands.size());
        assertEquals(60, demands.get(0).getMinQuantity());
    }

}

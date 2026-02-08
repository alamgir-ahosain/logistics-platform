package csembstu.alamgir.server.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import csembstu.alamgir.server.dto.request.CreateDemandRequest;
import csembstu.alamgir.server.dto.response.DemandResponse;
import csembstu.alamgir.server.entity.Demand;
import csembstu.alamgir.server.entity.Location;
import csembstu.alamgir.server.entity.Product;
import csembstu.alamgir.server.exception.NotFoundException;
import csembstu.alamgir.server.repository.DemandRepository;
import csembstu.alamgir.server.repository.LocationRepository;
import csembstu.alamgir.server.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class DemandServiceTest {

    @Mock             DemandRepository demandRepository;
    @InjectMocks  DemandService demandService;
    @Mock             LocationRepository locationRepository;
    @Mock             ProductRepository productRepository;

    private Location mockLocation;
    private Product mockProduct;
    private CreateDemandRequest mockDemandRequest;
    private Demand demand;


    @BeforeEach
    void init() {
        mockLocation = new Location() .setName("FreshMart Uttara").setType(LocationType.RETAILER) .setCity("Bogura");
        mockProduct = new Product().setName("Fresh Milk").setMaxTemperature(2.0).setMinTemperature(6.0);
        mockDemandRequest = new CreateDemandRequest().setDate("2026-02-07").setMinQuantity(60).setMaxQuantity(120);
        demand = new Demand().setDate("2026-02-07") .setMinQuantity(60).setMaxQuantity(120);
    }



    @Test
    @DisplayName("Create Demand: Should save successfully when Location and Product exist")
    void shouldSaveDemandSuccessfullyWhenLocationAndProductExist() {

        // GIVEN
        String locationId = "L1";
        String productId = "P1";

        mockLocation.setId(locationId);
        mockProduct.setId(productId);
        mockDemandRequest.setLocationId(locationId).setProductId(productId);


        when(locationRepository.findById(locationId)).thenReturn(Optional.of(mockLocation));
        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));
        
        demand.setId("D1").setLocation(mockLocation).setProduct(mockProduct);

        when(demandRepository.save(org.mockito.ArgumentMatchers.any(Demand.class))).thenReturn(demand);

        // WHEN
        DemandResponse response = demandService.createDemand(mockDemandRequest);

        // THEN
        assertNotNull(response);
        assertEquals("D1", response.getId()); 
        assertEquals(locationId, response.getLocationId());
        assertEquals(productId, response.getProductId());
        assertEquals(60, response.getMinQuantity());
        assertEquals(120, response.getMaxQuantity());
    }



    @Test
    @DisplayName("Should throw NotFoundException when Location does not exist")
    void shouldThrowNotFoundExceptionWhenLocationDoesNotExist() {

        // GIVEN
        String nonExistentLocationId = "Alamgir";
        String productId = "P1";

        mockDemandRequest.setLocationId(nonExistentLocationId) .setProductId(productId);

        when(locationRepository.findById(nonExistentLocationId)).thenReturn(Optional.empty());

        // WHEN
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            demandService.createDemand(mockDemandRequest);
        });

        // THEN
        assertEquals("Location does not exist: " + nonExistentLocationId, exception.getMessage());
    }



    @Test
    @DisplayName("Should throw NotFoundException when Product does not exist")
    void shouldThrowNotFoundExceptionWhenProductDoesNotExist() {

        // GIVEN
        String locationId = "L1";
        String nonExistentProductId = "Alamgir";

        mockDemandRequest.setLocationId(locationId) .setProductId(nonExistentProductId);

        when(locationRepository.findById(locationId)).thenReturn(Optional.of(mockLocation));
        when(productRepository.findById(nonExistentProductId)).thenReturn(Optional.empty());

        // WHEN
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            demandService.createDemand(mockDemandRequest);
        });

        // THEN
        assertEquals("Product does not exist: " + nonExistentProductId, exception.getMessage());
    }



    @Test
    @DisplayName("Get All Demand: Should return list of all Demand")
    void shouldReturnAllDemandSuccessfully() {

        // GIVEN
        String locationId = "L1";
        String productId = "P1";

        mockLocation.setId(locationId);
        mockProduct.setId(productId);

        Demand demand1 = new Demand()
                .setId("D1")
                .setLocation(mockLocation)
                .setProduct(mockProduct)
                .setDate("2026-02-07")
                .setMinQuantity(60)
                .setMaxQuantity(120);

        Demand demand2 = new Demand()
                .setId("D2")
                .setLocation(mockLocation)
                .setProduct(mockProduct)
                .setDate("2026-02-17")
                .setMinQuantity(70)
                .setMaxQuantity(220);

        when(demandRepository.findAll()).thenReturn(java.util.List.of(demand1, demand2));

        // WHEN
        List<DemandResponse> responses = demandService.getAllDemand();

        // THEN
        assertNotNull(responses);
        assertEquals(2, responses.size());

        DemandResponse first = responses.get(0);
        assertEquals("D1", first.getId());
        assertEquals(locationId, first.getLocationId());
        assertEquals(productId, first.getProductId());
        assertEquals(60, first.getMinQuantity());
        assertEquals(120, first.getMaxQuantity());

        // Verify
        verify(demandRepository, times(1)).findAll();
    }

}

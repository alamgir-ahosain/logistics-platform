package csembstu.alamgir.server.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import csembstu.alamgir.server.dto.request.DateRequestValidation;
import csembstu.alamgir.server.dto.response.FullLogisticsValidationResponse;
import csembstu.alamgir.server.dto.response.StorageCapacityViolation;
import csembstu.alamgir.server.entity.*;
import csembstu.alamgir.server.repository.*;

@ExtendWith(MockitoExtension.class)
class ValidationApiService2Test {

        @InjectMocks  private ValidationApiService2 service;

        @Mock   private DemandRepository demandRepository;
        @Mock   private RouteRepository routeRepository;
        @Mock   private StorageUnitRepository storageUnitRepository;

        private Product product;
        private Location destination;
        private Location warehouse;
        private Demand demand;
        private Route route;
        private StorageUnit unit;



        @BeforeEach
        void init() {
                product = new Product().setName("Ice Cream").setMinTemperature(-10) .setMaxTemperature(-5);
                destination = new Location() .setId("L2") .setName("Shop");
                warehouse = new Location() .setId("W1") .setName("Warehouse A");
                demand = new Demand() .setMinQuantity(10)  .setMaxQuantity(20);
                route = new Route()  .setCapacity(100)    .setMinShipment(5);
                unit = new StorageUnit() .setMinTemperature(5); // temperature incompatible  .setMaxTemperature(25)  .setCapacity(200);
        }



        @Test
        @DisplayName("Should detect temperature incompatible demand")
        void temperatureIncompatibleDemand() {

                // GIVEN
                demand.setProduct(product).setLocation(destination);
                route.setFromLocation(warehouse).setToLocation(destination);
     
                when(demandRepository.findByDate(any())).thenReturn(List.of(demand));
                when(routeRepository.findByToLocation_Id("L2")).thenReturn(List.of(route));
                when(storageUnitRepository.findByLocation_Id(route.getFromLocation().getId())).thenReturn(List.of(unit));

                // WHEN
                FullLogisticsValidationResponse response = service.fullLogisticsValidation(new DateRequestValidation());

                // THEN
                String expectedError = "Product Ice Cream has no safe warehouse storage to deliver to Shop";
                assertEquals(expectedError, response.getTemperature_incompatible_demands().get(0));
                assertEquals(1, response.getTemperature_incompatible_demands().size());
                assertTrue(response.getCapacity_violations().isEmpty());
                assertTrue(response.getStorage_capacity_violations().isEmpty());
        }



        @Test
        @DisplayName("Should detect capacity violations")
        void capacityViolationDetected() {

                // GIVEN
                product.setMinTemperature(2).setMaxTemperature(8);
                demand.setProduct(product) .setLocation(destination) .setMinQuantity(50) .setMaxQuantity(200);
                route.setFromLocation(warehouse) .setToLocation(destination).setCapacity(100); // max violated.setMinShipment(60); // min violated
                unit.setMinTemperature(0).setMaxTemperature(10).setCapacity(500);

                when(demandRepository.findByDate(any())).thenReturn(List.of(demand));
                when(routeRepository.findByToLocation_Id("L2")).thenReturn(List.of(route));
                when(storageUnitRepository.findByLocation_Id(route.getFromLocation().getId())).thenReturn(List.of(unit));

                // WHEN
                FullLogisticsValidationResponse response = service.fullLogisticsValidation(new DateRequestValidation());

                // THEN
                assertEquals(2, response.getCapacity_violations().size(), "Should have detected 2 capacity violations");

                boolean hasMinError = response.getCapacity_violations().stream().anyMatch(v -> v.getViolation().equals("MIN_CAPACITY_VIOLATION"));
                boolean hasMaxError = response.getCapacity_violations().stream().anyMatch(v -> v.getViolation().equals("MAX_CAPACITY_VIOLATION"));
                assertTrue(hasMinError);
                assertTrue(hasMaxError);
        }




        @Test
        @DisplayName("Should detect storage capacity overflow")
        void storageCapacityViolationDetected() {

                //GIVEN
                product.setMinTemperature(2).setMaxTemperature(8);
                demand.setProduct(product).setLocation(destination).setMinQuantity(10).setMaxQuantity(300); // exceeds storage
                route.setFromLocation(warehouse) .setToLocation(destination) .setCapacity(500).setMinShipment(5);
                unit.setMinTemperature(0) .setMaxTemperature(10) .setCapacity(200);// total storage < demand max

                when(demandRepository.findByDate(any())).thenReturn(List.of(demand));
                when(routeRepository.findByToLocation_Id("L2")).thenReturn(List.of(route));
                when(storageUnitRepository.findByLocation_Id("W1")).thenReturn(List.of(unit));

                //WHEN
                FullLogisticsValidationResponse response = service.fullLogisticsValidation(new DateRequestValidation());

                // THEN
                StorageCapacityViolation violation = response.getStorage_capacity_violations().get(0);
                assertEquals("Warehouse A", violation.getWarehouse());
                assertEquals(200, violation.getStorage_capacity());
                assertEquals(300, violation.getTotal_required_capacity());
                assertEquals(1, response.getStorage_capacity_violations().size());
        }
}

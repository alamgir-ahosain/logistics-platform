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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import csembstu.alamgir.server.dto.LocationType;
import csembstu.alamgir.server.dto.request.CreateStorageUnitRequest;
import csembstu.alamgir.server.dto.response.StorageUnitResponse;
import csembstu.alamgir.server.entity.Location;
import csembstu.alamgir.server.entity.StorageUnit;
import csembstu.alamgir.server.exception.NotFoundException;
import csembstu.alamgir.server.repository.LocationRepository;
import csembstu.alamgir.server.repository.StorageUnitRepository;

@ExtendWith(MockitoExtension.class)
public class StorageUnitServiceTest {

        @Mock
        private StorageUnitRepository storageUnitRepository;

        @InjectMocks
        private StorageUnitService storageUnitService;

        @Mock
        private LocationRepository locationRepository;

        private Location mockLocation1;
        private Location mockLocation2;
        private Location mockLocation3;

        private CreateStorageUnitRequest mockStorageUnit1;
        private CreateStorageUnitRequest mockStorageUnit2;
        private CreateStorageUnitRequest mockStorageUnit3;

        @BeforeEach
        void init() {
                mockLocation1 = new Location().setId("loc-1").setName("Central Cold Warehouse").setType(LocationType.WAREHOUSE).setCity("Dhaka");
                mockLocation2 = new Location().setId("loc-2").setName("North Storage Hub").setType(LocationType.WAREHOUSE).setCity("Rajshahi");
                mockLocation3 = new Location().setId("loc-3").setName(" South Distribution Hub").setType(LocationType.WAREHOUSE).setCity("Khulna");

                mockStorageUnit1 = new CreateStorageUnitRequest().setMinTemperature(-10.0).setMaxTemperature(-5.0).setCapacity(500);
                mockStorageUnit2 = new CreateStorageUnitRequest().setMinTemperature(0.0).setMaxTemperature(10.0).setCapacity(300);
                mockStorageUnit3 = new CreateStorageUnitRequest().setMinTemperature(-22.0).setMaxTemperature(-6.0).setCapacity(250);
        }



        private StorageUnit mapToEntity(CreateStorageUnitRequest request, String id) {
                StorageUnit unit = new StorageUnit();
                unit.setId(id).setLocation(mockLocation1).setMinTemperature(request.getMinTemperature()).setMaxTemperature(request.getMaxTemperature()).setCapacity(request.getCapacity());
                return unit;
        }




        @Test
        @DisplayName("Create StorageUnit: Should save successfully when Location is exist")
        void shouldSaveStorageUnitSuccessfullyWhenLocationExist() {

                // GIVEN
                mockStorageUnit1.setLocationId("loc-1");
                when(locationRepository.findById("loc-1")).thenReturn(java.util.Optional.of(mockLocation1));
                when(storageUnitRepository.save(any(StorageUnit.class)))
                                .thenAnswer(invocation -> {
                                        StorageUnit unit = invocation.getArgument(0);
                                        unit.setId("w1");
                                        return unit;
                                });
                // WHEN
                StorageUnitResponse response = storageUnitService.createStorageUnit(mockStorageUnit1);

                // THEN
                assertNotNull(response);
                assertEquals("w1", response.getId());
                assertEquals("loc-1", response.getLocationId());
                assertEquals(-10.0, response.getMinTemperature());
                assertEquals(-5.0, response.getMaxTemperature());
                assertEquals(500, response.getCapacity());
        }




        @Test
        @DisplayName("Create StorageUnit: should throw NotFoundException when location does not exist")
        void shouldThrowNotFoundExceptionWhenLocationDoesNotExist() {

                // GIVEN
                String nonExistentLocationId = "loc-12";
                when(locationRepository.findById(nonExistentLocationId)).thenReturn(java.util.Optional.empty());
                mockStorageUnit1.setLocationId(nonExistentLocationId);

                // WHEN & THEN
                NotFoundException exception = assertThrows(NotFoundException.class, () -> {
                        storageUnitService.createStorageUnit(mockStorageUnit1);
                });
                
                assertEquals("Location does not exist: " + nonExistentLocationId, exception.getMessage());

                // VERIFY
                verify(storageUnitRepository, never()).save(any(StorageUnit.class));
        }




        @Test
        @DisplayName("Get All StorageUnit: Should return list of all StorageUnit")
        void shouldReturnAllStorageUnitSuccessfully() {
               
                // GIVEN
                StorageUnit unit1 = mapToEntity(mockStorageUnit1, "w1");
                StorageUnit unit2 = mapToEntity(mockStorageUnit1, "w2");
                when(storageUnitRepository.findAll()).thenReturn(asList(unit1, unit2));

                // WHEN
                List<StorageUnitResponse> responses = storageUnitService.getAllStorageUnit();

                // THEN
                assertNotNull(responses);
                assertEquals(2, responses.size());

                // VERIFY
                verify(storageUnitRepository, times(1)).findAll();
        }
}

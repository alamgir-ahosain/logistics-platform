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
import csembstu.alamgir.server.entity.StorageUnit;

@DataJpaTest
public class StorageUnitRepositoryTest {

    @Autowired   LocationRepository locationRepository;
    @Autowired   StorageUnitRepository storageUnitRepository;

    private Location mockLocation1;
    private Location mockLocation2;
    private Location mockLocation3;

    private StorageUnit mockStorageUnit1;
    private StorageUnit mockStorageUnit2;
    private StorageUnit mockStorageUnit3;




    @BeforeEach
    void init() {
        mockLocation1= new Location();     mockLocation1.setName("Central Cold Warehouse");     mockLocation1.setType(LocationType.WAREHOUSE);    mockLocation1.setCity("Dhaka");
        mockLocation2 = new Location();   mockLocation2.setName("North Storage Hub");              mockLocation2.setType(LocationType.WAREHOUSE);    mockLocation2.setCity("Rajshahi");
        mockLocation3 = new Location();   mockLocation3.setName(" South Distribution Hub");      mockLocation3.setType(LocationType.WAREHOUSE);    mockLocation3.setCity("Khulna");

        mockStorageUnit1 = new StorageUnit().setLocation(mockLocation1)  .setMinTemperature(-25.0) .setMaxTemperature(-5.0).setCapacity(500);
        mockStorageUnit2 = new StorageUnit().setLocation(mockLocation1) .setMinTemperature(0.0)   .setMaxTemperature(10.0) .setCapacity(300);
        mockStorageUnit3 = new StorageUnit().setLocation(mockLocation2) .setMinTemperature(-22.0)   .setMaxTemperature(-6.0) .setCapacity(250);
    }



    @Test
    @DisplayName("Should return storage unit matching location")
    void shouldReturnStorageUnitThatMatch() {
        
        locationRepository.save(mockLocation1);             locationRepository.save(mockLocation2);
        storageUnitRepository.save(mockStorageUnit1);  storageUnitRepository.save(mockStorageUnit2);  storageUnitRepository.save(mockStorageUnit3);

        List<StorageUnit> storageUnits = storageUnitRepository.findByLocation_Id(mockLocation1.getId());

        assertNotNull(storageUnits);
        assertEquals(2, storageUnits.size());
    }


    @Test
    @DisplayName(" Should return Null when Location does not exists")
    void shouldReturnNullWhenLocationIdIsIncorrect() {
        List<StorageUnit> storageUnits = storageUnitRepository.findByLocation_Id("ultapalta");

        assertEquals(0, storageUnits.size());
    }
}

package csembstu.alamgir.server.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import csembstu.alamgir.server.dto.LocationType;
import csembstu.alamgir.server.dto.request.CreateStorageUnitRequest;
import csembstu.alamgir.server.dto.response.StorageUnitResponse;
import csembstu.alamgir.server.entity.Location;
import csembstu.alamgir.server.entity.StorageUnit;
import csembstu.alamgir.server.exception.BadRequestException;
import csembstu.alamgir.server.exception.NotFoundException;
import csembstu.alamgir.server.repository.LocationRepository;
import csembstu.alamgir.server.repository.StorageUnitRepository;


@Service
public class StorageUnitService {

    @Autowired
    private StorageUnitRepository storageUnitRepository;

    @Autowired
    private LocationRepository locationRepository;



    public StorageUnitResponse createStorageUnit(CreateStorageUnitRequest request) {

        //  404: Location does not exist
        Location location = locationRepository.findById(request.getLocationId()).orElseThrow(() -> new NotFoundException("Location does not exist: " + request.getLocationId()));
        
        //  400: Location ID does not belong to type WAREHOUSE
        if (!location.getType().equals(LocationType.WAREHOUSE)) throw new BadRequestException("Location ID does not belong to type WAREHOUSE");
    
        StorageUnit newStorageUnit = new StorageUnit();
        newStorageUnit.setLocation(location)
                                   .setCapacity(request.getCapacity())
                                   .setMaxTemperature(request.getMaxTemperature())
                                   .setMinTemperature(request.getMinTemperature());

        storageUnitRepository.save(newStorageUnit);

        return mapToResponseHelper(newStorageUnit);

    }




    public List<StorageUnitResponse> getAllStorageUnit() {

        List<StorageUnitResponse> responseList = new ArrayList<>();

        List<StorageUnit> storageUnits = storageUnitRepository.findAll();
        for (StorageUnit storageUnit : storageUnits) {
            StorageUnitResponse dto = mapToResponseHelper(storageUnit);
            responseList.add(dto);
        }
        return responseList;
    }

    public List<StorageUnit> getAllStorageUnits(){
        return storageUnitRepository.findAll();
    } 

        private StorageUnitResponse mapToResponseHelper(StorageUnit newStorageUnit) {

        StorageUnitResponse response=new StorageUnitResponse();
        response.setId(newStorageUnit.getId())
                        .setLocationId(newStorageUnit.getLocation().getId())
                        .setMaxTemperature(newStorageUnit.getMaxTemperature())
                        .setMinTemperature(newStorageUnit.getMinTemperature())
                        .setCapacity(newStorageUnit.getCapacity());

        return response;
    }


}


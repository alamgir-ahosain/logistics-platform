package csembstu.alamgir.server.controller;

import org.springframework.web.bind.annotation.RestController;

import csembstu.alamgir.server.dto.request.CreateStorageUnitRequest;
import csembstu.alamgir.server.dto.response.StorageUnitResponse;
import csembstu.alamgir.server.entity.StorageUnit;
import csembstu.alamgir.server.service.StorageUnitService;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class StorageUnitController {
    
    @Autowired
    private StorageUnitService storageUnitService;

    @PostMapping("api/storage-units")
    public ResponseEntity<StorageUnitResponse> createStorageUnit(@Valid @RequestBody CreateStorageUnitRequest request) {
        StorageUnitResponse newStorageUnit = storageUnitService.createStorageUnit(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newStorageUnit);
    }

    @GetMapping("api/storage-units")
    public List<StorageUnitResponse> getAllStorageUnit() {return storageUnitService.getAllStorageUnit();}
    
    
    @GetMapping("api/storage-units/all")
    public List<StorageUnit>  getAllStorageUnits() {return storageUnitService.getAllStorageUnits();}


}

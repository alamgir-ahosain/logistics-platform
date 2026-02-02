package csembstu.alamgir.server.controller;



import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import csembstu.alamgir.server.dto.request.CreateLocationRequest;
import csembstu.alamgir.server.entity.Location;
import csembstu.alamgir.server.service.LocationService;
import jakarta.validation.Valid;

@RestController
public class LocationController {

    @Autowired
    private LocationService locationService;

    @PostMapping("api/location")
    public ResponseEntity<Location> createLocation(@Valid @RequestBody CreateLocationRequest request)throws BadRequestException {
        Location location = locationService.createLocation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(location); // 201 Created
    }

    @GetMapping("/api/location")
    public List<Location> getAllLocations() { return locationService.getAllLocations(); }

}

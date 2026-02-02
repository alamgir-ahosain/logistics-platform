package csembstu.alamgir.server.service;

import java.util.List;

import org.apache.coyote.BadRequestException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import csembstu.alamgir.server.dto.request.CreateLocationRequest;
import csembstu.alamgir.server.entity.Location;
import csembstu.alamgir.server.repository.LocationRepository;


@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    public Location createLocation(CreateLocationRequest request) throws BadRequestException {

        boolean existsByNameAndCity=locationRepository.existsByNameAndCity(request.getName(), request.getCity());
        if (existsByNameAndCity) throw new BadRequestException("Location '" + request.getName() + "' already exists in " + request.getCity());


        Location newLocation = new Location();
        newLocation.setName(request.getName());
        newLocation.setType(request.getType());
        newLocation.setCity(request.getCity());

        locationRepository.save(newLocation);

        return newLocation;
    }

    
    public List<Location> getAllLocations() {return locationRepository.findAll();  }

}

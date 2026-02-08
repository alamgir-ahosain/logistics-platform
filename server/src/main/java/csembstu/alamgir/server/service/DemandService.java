package csembstu.alamgir.server.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import csembstu.alamgir.server.dto.LocationType;
import csembstu.alamgir.server.dto.request.CreateDemandRequest;
import csembstu.alamgir.server.dto.response.DemandResponse;
import csembstu.alamgir.server.entity.Demand;
import csembstu.alamgir.server.entity.Location;
import csembstu.alamgir.server.entity.Product;
import csembstu.alamgir.server.exception.BadRequestException;
import csembstu.alamgir.server.exception.NotFoundException;
import csembstu.alamgir.server.repository.DemandRepository;
import csembstu.alamgir.server.repository.LocationRepository;
import csembstu.alamgir.server.repository.ProductRepository;

@Service
public class DemandService {

    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    DemandRepository demandRepository;

    @Autowired
    private ProductRepository productRepository;





    public DemandResponse createDemand(CreateDemandRequest request) {

        Location location = locationRepository.findById(request.getLocationId()).orElseThrow(() -> new NotFoundException("Location does not exist: " + request.getLocationId()));
        Product product = productRepository.findById(request.getProductId()).orElseThrow(() -> new NotFoundException("Product does not exist: " + request.getProductId()));

        // demands only for RETAILER/HOSPITAL
        if (!location.getType().equals(LocationType.RETAILER) && !location.getType().equals(LocationType.HOSPITAL)) {
            throw new BadRequestException("Demands can only be created for RETAILER or HOSPITAL locations");
        }

        Demand newDemand = new Demand();
        newDemand.setLocation(location)
                .setProduct(product)
                .setDate(request.getDate())
                .setMaxQuantity((int) request.getMaxQuantity())
                .setMinQuantity((int) request.getMinQuantity());

        Demand savedDemand=demandRepository.save(newDemand);
        return mapToResponseHelper(savedDemand);

    }





    public List<DemandResponse> getAllDemand() {

        List<DemandResponse> responseList = new ArrayList<>();

        List<Demand> demands = demandRepository.findAll();
        for (Demand demand : demands) {
            DemandResponse dto = mapToResponseHelper(demand);
            responseList.add(dto);

        }
        return responseList;
    }




    
    public List<Demand> getAllDemands() {
        return demandRepository.findAll();
    }




    private DemandResponse mapToResponseHelper(Demand newDemand) {

        DemandResponse response = new DemandResponse();

        response.setId(newDemand.getId())
                .setLocationId(newDemand.getLocation().getId())
                .setProductId(newDemand.getProduct().getId())
                .setDate(newDemand.getDate())
                .setMinQuantity(newDemand.getMinQuantity())
                .setMaxQuantity(newDemand.getMaxQuantity());
        return response;
    }

}

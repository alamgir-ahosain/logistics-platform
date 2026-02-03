package csembstu.alamgir.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import csembstu.alamgir.server.dto.response.DemandResponse;
import csembstu.alamgir.server.dto.response.LogisticsSummaryResponse;
import csembstu.alamgir.server.dto.response.RouteResponse;
import csembstu.alamgir.server.dto.response.StorageUnitResponse;
import csembstu.alamgir.server.entity.Demand;

import csembstu.alamgir.server.entity.Route;
import csembstu.alamgir.server.entity.StorageUnit;
import csembstu.alamgir.server.repository.DemandRepository;
import csembstu.alamgir.server.repository.LocationRepository;
import csembstu.alamgir.server.repository.ProductRepository;
import csembstu.alamgir.server.repository.RouteRepository;
import csembstu.alamgir.server.repository.StorageUnitRepository;

@Service
public class LogisticsSummaryService {

    @Autowired   private LocationRepository locationRepository;
    @Autowired   private ProductRepository productRepository;
    @Autowired   private StorageUnitRepository storageUnitRepository;
    @Autowired   private RouteRepository routeRepository;
    @Autowired   private DemandRepository demandRepository;

    public LogisticsSummaryResponse logisticsSummaryResponse() {
        LogisticsSummaryResponse summary = new LogisticsSummaryResponse();

        // Fetch everything from the database
        summary.setLocations(locationRepository.findAll());
        summary.setProducts(productRepository.findAll());
        summary.setStorageUnits(storageUnitRepository.findAll().stream().map(this::mapToResponseHelper_1).toList());
        summary.setRoutes(routeRepository.findAll().stream().map(this::mapToResponseHelper_2).toList());
        summary.setDemands(demandRepository.findAll().stream().map(this::mapToResponseHelper_3).toList());
        return summary;

    }

    private StorageUnitResponse mapToResponseHelper_1(StorageUnit newStorageUnit) {

        StorageUnitResponse response = new StorageUnitResponse();
        response.setId(newStorageUnit.getId())
                .setLocationId(newStorageUnit.getLocation().getId())
                .setMaxTemperature(newStorageUnit.getMaxTemperature())
                .setMinTemperature(newStorageUnit.getMinTemperature())
                .setCapacity(newStorageUnit.getCapacity());

        return response;
    }

    private RouteResponse mapToResponseHelper_2(Route newRoute) {
        RouteResponse response = new RouteResponse();
        response.setId(newRoute.getId())
                .setFromLocationId(newRoute.getFromLocation().getId())
                .setToLocationId(newRoute.getToLocation().getId())
                .setCapacity(newRoute.getCapacity())
                .setMinShipment(newRoute.getMinShipment());
        return response;
    }

    private DemandResponse mapToResponseHelper_3(Demand newDemand) {

        DemandResponse response = new DemandResponse();

        response.setId(newDemand.getId())
                .setLocationId(newDemand.getLocation().getId())
                .setProductId(newDemand.getId())
                .setDate(newDemand.getDate())
                .setMinQuantity(newDemand.getMinQuantity())
                .setMaxQuantity(newDemand.getMaxQuantity());
        return response;
    }

}
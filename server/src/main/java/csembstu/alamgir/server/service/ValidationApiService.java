package csembstu.alamgir.server.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import csembstu.alamgir.server.dto.request.DateRequestValidation;
import csembstu.alamgir.server.dto.response.TemperatureValidationResponse;
import csembstu.alamgir.server.entity.Demand;
import csembstu.alamgir.server.entity.Location;
import csembstu.alamgir.server.entity.Product;
import csembstu.alamgir.server.entity.Route;
import csembstu.alamgir.server.entity.StorageUnit;
import csembstu.alamgir.server.repository.DemandRepository;
import csembstu.alamgir.server.repository.RouteRepository;
import csembstu.alamgir.server.repository.StorageUnitRepository;

@Service
public class ValidationApiService {

    @Autowired  private DemandRepository demandRepository;
    @Autowired  private RouteRepository routeRepository;
    @Autowired private StorageUnitRepository storageUnitRepository;





    /*
         product.minTemp >= storageUnit.minTemp
         product.maxTemp <= storageUnit.maxTemp
         Demand -> Routes(toLocationId) -> Warehouses -> StorageUnits -> Check temperature Compatibility
     */
    public TemperatureValidationResponse validateTemperature(DateRequestValidation request) {

        List<Demand> demands = demandRepository.findByDate(request.getDate());
        List<String> issues = new ArrayList<>();

        for (Demand demand : demands) {
            Product product = demand.getProduct();
            Location destination = demand.getLocation();

            // 1. Get all routes leading to this specific destination
            List<Route> routesToDestination = routeRepository.findByToLocation_Id(destination.getId());

            // 2. Find ONE compatible WAREHOUSE to pass
            boolean atLeastOnePathIsValid = false;

            for (Route route : routesToDestination) {
                Location sourceWarehouse = route.getFromLocation();
                List<StorageUnit> units = storageUnitRepository.findByLocation_Id(sourceWarehouse.getId());

                boolean warehouseCanHandle = units.stream()
                                .anyMatch(unit -> 
                                    product.getMinTemperature() >= unit.getMinTemperature() &&
                                    product.getMaxTemperature() <= unit.getMaxTemperature());

                if (warehouseCanHandle) {
                    atLeastOnePathIsValid = true;
                    break; // Stop looking at other routes for this product
                }
            }

            // 3. If after checking ALL routes, none work, then record the issue
            if (!atLeastOnePathIsValid) {
                issues.add(String.format("Product %s is temperature incompatible with all available storage locations for %s", product.getName(), destination.getName()));
            }
        }
        return new TemperatureValidationResponse(issues.isEmpty(), issues);
    }




}

package csembstu.alamgir.server.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import csembstu.alamgir.server.dto.request.DateRequestValidation;
import csembstu.alamgir.server.dto.response.*;
import csembstu.alamgir.server.entity.*;
import csembstu.alamgir.server.repository.*;

@Service
public class ValidationApiService2 {

    @Autowired  private DemandRepository demandRepository;
    @Autowired  private RouteRepository routeRepository;
    @Autowired  private StorageUnitRepository storageUnitRepository;



    

    // Capacity Validation (Dependent on Temperature)
    public FullLogisticsValidationResponse  fullLogisticsValidation(DateRequestValidation request) {

        List<Demand> demands = demandRepository.findByDate(request.getDate());
        FullLogisticsValidationResponse response = new FullLogisticsValidationResponse();

        // Track total capacity usage per Warehouse (Map<WarehouseLocationId,TotalQuantity>)
        Map<String, Integer> warehouseLoadMap = new HashMap<>();
        Map<String, String> warehouseNameMap = new HashMap<>();
        Map<String, Integer> warehouseTotalCapacityMap = new HashMap<>();

        for (Demand demand : demands) {
            Product product = demand.getProduct();
            Location destination = demand.getLocation();
            List<Route> allRoutes = routeRepository.findByToLocation_Id(destination.getId());

            // 1. FILTER: Only look at routes that passed temperature validation
            List<Route> safeRoutes = findTempCompatibleRoutes(product, allRoutes);

            // If no routes are temperature safe, capacity is irrelevant
            if (safeRoutes.isEmpty()) {
                response.getTemperature_incompatible_demands().add(String.format("Product %s has no safe warehouse storage to deliver to %s",product.getName(), destination.getName()));
                continue;
            }

            // 2. CHECK: Of the safe routes, do any satisfy min/max quantity?
            boolean capacityPassed = safeRoutes.stream()
                    .anyMatch(route -> 
                            demand.getMaxQuantity() <= route.getCapacity() &&
                            demand.getMinQuantity() >= route.getMinShipment());

            if (!capacityPassed) {
                for (Route route : safeRoutes) {
                    
                    if (demand.getMinQuantity() < route.getMinShipment()) 
                        response.getCapacity_violations().add(new CapacityViolation("MIN_CAPACITY_VIOLATION", route.getFromLocation().getName(),destination.getName(), demand.getMinQuantity(), route.getMinShipment()));

                    if (demand.getMaxQuantity() > route.getCapacity()) 
                        response.getCapacity_violations().add(new CapacityViolation("MAX_CAPACITY_VIOLATION", route.getFromLocation().getName(),destination.getName(), demand.getMinQuantity(), route.getMinShipment()));
                    
                }

            }

            // 3. CHECK Storage Load 
            if (!safeRoutes.isEmpty()) {
                Location source = safeRoutes.get(0).getFromLocation();
                warehouseLoadMap.merge(source.getId(), demand.getMaxQuantity(), Integer::sum);
                warehouseNameMap.put(source.getId(), source.getName());

                // Get sum of capacity of all StorageUnits at this warehouse
                int totalStorage = storageUnitRepository.findByLocation_Id(source.getId()).stream().mapToInt(StorageUnit::getCapacity).sum();
                warehouseTotalCapacityMap.put(source.getId(), totalStorage);
            }
        }

        // 4. Final Storage Violation Check
        warehouseLoadMap.forEach((locId, load) -> {
            int capacity = warehouseTotalCapacityMap.get(locId);
            if (load > capacity) {
                response.getStorage_capacity_violations().add(new StorageCapacityViolation(warehouseNameMap.get(locId), load, capacity));
            }
        });

        return response;
    }

    

    // Find ONE compatible WAREHOUSE to pass
    private List<Route> findTempCompatibleRoutes(Product product, List<Route> routes) {
        List<Route> compatibleRoutes = new ArrayList<>();
        for (Route route : routes) {
            List<StorageUnit> units = storageUnitRepository.findByLocation_Id(route.getFromLocation().getId());

            boolean canStore = units.stream()
                    .anyMatch(unit -> 
                            product.getMinTemperature() >= unit.getMinTemperature() &&
                            product.getMaxTemperature() <= unit.getMaxTemperature());
            if (canStore)  compatibleRoutes.add(route);
        }
        return compatibleRoutes;
    }
}
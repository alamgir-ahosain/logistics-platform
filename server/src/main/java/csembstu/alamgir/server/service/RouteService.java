package csembstu.alamgir.server.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import csembstu.alamgir.server.dto.LocationType;
import csembstu.alamgir.server.dto.request.CreateRouteRequest;
import csembstu.alamgir.server.dto.response.RouteResponse;
import csembstu.alamgir.server.entity.Location;
import csembstu.alamgir.server.entity.Route;
import csembstu.alamgir.server.exception.BadRequestException;
import csembstu.alamgir.server.exception.NotFoundException;
import csembstu.alamgir.server.repository.LocationRepository;
import csembstu.alamgir.server.repository.RouteRepository;

@Service
public class RouteService {

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private LocationRepository locationRepository;




    public RouteResponse createRoute(CreateRouteRequest request) {

        Location fromLocation = locationRepository .findById(request.getFromLocationId()) .orElseThrow(() -> new NotFoundException("Location does not exist: " + request.getFromLocationId()));
        Location toLocation = locationRepository .findById(request.getToLocationId()) .orElseThrow(() -> new NotFoundException("Location does not exist: " + request.getToLocationId()));

        LocationType from = fromLocation.getType();
        LocationType to = toLocation.getType();

        boolean validRoute = false;

        if (from.equals(LocationType.PRODUCER) && to.equals(LocationType.WAREHOUSE))  validRoute = true;
        else if (from.equals(LocationType.WAREHOUSE) && to.equals(LocationType.RETAILER))  validRoute = true;
        else if (from.equals(LocationType.WAREHOUSE) && to.equals(LocationType.HOSPITAL))  validRoute = true;

        if (!validRoute)  throw new BadRequestException( "INVALID ROUTE::: " + from + "-" + to+ " , Routes can only be between PRODUCER-WAREHOUSE, WAREHOUSE-RETAILER, or WAREHOUSE-HOSPITA");

        Route newRoute = new Route();
        newRoute.setFromLocation(fromLocation);   newRoute.setToLocation(toLocation);   newRoute.setMinShipment(request.getMinShipment());   newRoute.setCapacity(request.getCapacity());

        routeRepository.save(newRoute);
        return mapToResponseHelper(newRoute);






    }






    public List<RouteResponse> getAllRoute() {

        List<RouteResponse> responseList = new ArrayList<>();

        List<Route> routes = routeRepository.findAll();
        for (Route route : routes) {
            RouteResponse dto = mapToResponseHelper(route);
            responseList.add(dto);
        }
        return responseList;
    }


    public List<Route> getAllRoutes() {   return routeRepository.findAll(); }


    private RouteResponse mapToResponseHelper(Route newRoute) {
        RouteResponse response = new RouteResponse();
        response.setId(newRoute.getId())
                        .setFromLocationId(newRoute.getFromLocation().getId())
                        .setToLocationId(newRoute.getToLocation().getId())
                        .setCapacity(newRoute.getCapacity())
                        .setMinShipment(newRoute.getMinShipment());
        return response;
    }

}

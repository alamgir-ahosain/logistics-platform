package csembstu.alamgir.server.controller;

import org.springframework.web.bind.annotation.RestController;

import csembstu.alamgir.server.dto.request.CreateRouteRequest;
import csembstu.alamgir.server.dto.response.RouteResponse;
import csembstu.alamgir.server.entity.Route;
import csembstu.alamgir.server.service.RouteService;
import jakarta.validation.Valid;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class RouteController {

    @Autowired
    private RouteService routeService;

    @PostMapping("api/routes")
    public ResponseEntity<RouteResponse> createRoute(@Valid @RequestBody CreateRouteRequest request) {
        RouteResponse newRoute = routeService.createRoute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newRoute);
    }

    @GetMapping("api/routes")
    public List<RouteResponse> getAllRoute() {  return routeService.getAllRoute(); }


    @GetMapping("api/routes/all")
    public List<Route> getAllRoutes() {  return routeService.getAllRoutes();  }

}

package csembstu.alamgir.server.controller;

import org.springframework.web.bind.annotation.RestController;

import csembstu.alamgir.server.dto.request.CreateDemandRequest;
import csembstu.alamgir.server.dto.response.DemandResponse;
import csembstu.alamgir.server.entity.Demand;
import csembstu.alamgir.server.service.DemandService;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController

public class DemandController {

    @Autowired
    private DemandService demandService;

    @PostMapping("api/demands")
    public ResponseEntity<DemandResponse> createDemand(@Valid @RequestBody CreateDemandRequest req) {
        DemandResponse demand = demandService.createDemand(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(demand);
    }

    @GetMapping("api/demands")
    public List<DemandResponse> getAllDemand() {  return demandService.getAllDemand();  }

    @GetMapping("api/demands/all")
    public List<Demand> getAllDemands() { return demandService.getAllDemands(); }

}

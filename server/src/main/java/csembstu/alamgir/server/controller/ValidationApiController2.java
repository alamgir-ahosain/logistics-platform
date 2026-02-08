package csembstu.alamgir.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import csembstu.alamgir.server.dto.request.DateRequestValidation;
import csembstu.alamgir.server.dto.response.FullLogisticsValidationResponse;
import csembstu.alamgir.server.service.ValidationApiService2;

@RestController
public class ValidationApiController2 {

    @Autowired
    private ValidationApiService2 validationApiService2;

    @PostMapping("/full-logistics-validation")
    public FullLogisticsValidationResponse validateCap(@RequestBody DateRequestValidation request) {
        return validationApiService2.fullLogisticsValidation(request);
    }
}
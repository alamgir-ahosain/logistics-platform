package csembstu.alamgir.server.controller;

import org.springframework.web.bind.annotation.RestController;

import csembstu.alamgir.server.dto.request.DateRequestValidation;
import csembstu.alamgir.server.dto.response.TemperatureValidationResponse;

import csembstu.alamgir.server.service.ValidationApiService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class ValidationApiController {

    @Autowired
    private ValidationApiService validationApiService;

    @PostMapping("api/temps/validate")
    public TemperatureValidationResponse postMethodName(@Valid @RequestBody DateRequestValidation requestValidation) {
        return validationApiService.validateTemperature(requestValidation);
    }

}
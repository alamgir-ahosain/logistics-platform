package csembstu.alamgir.server.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateStorageUnitRequest {

    @NotBlank(message = "Location ID is required")
    private String locationId;

    @NotNull(message = "Minimum temperature is required")
    private Double minTemperature;

    @NotNull(message = "Maximum temperature is required")
    private Double maxTemperature;

    @NotNull(message = "Capacity is required")
    private Integer capacity;
}

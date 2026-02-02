package csembstu.alamgir.server.dto.request;

import csembstu.alamgir.server.dto.LocationType;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateLocationRequest {

    @NotBlank(message = "Name is required")  private String name;
    @NotNull    private LocationType type; // PRODUCER, WAREHOUSE, RETAILER, HOSPITAL
    @NotBlank(message = "City is required") private String city;
}

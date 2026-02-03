package csembstu.alamgir.server.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateProductRequest {

    @NotBlank(message = "Product name is required")
    private String name;

    @NotNull(message = "Minimum temperature is required")
    private Double minTemperature;

    @NotNull(message = "Maximum temperature is required")
    private Double maxTemperature;
}

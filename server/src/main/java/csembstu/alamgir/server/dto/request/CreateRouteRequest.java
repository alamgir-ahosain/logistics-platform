package csembstu.alamgir.server.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateRouteRequest {

    @NotBlank(message = "fromLocationId  is required")
    private String fromLocationId;

    @NotBlank(message = "toLocationId is required")
    private String toLocationId;

    @NotNull(message = "capacity  is required")
    private Integer capacity;

    @NotNull(message = "minShipment  is required")
    private Integer minShipment;

}

package csembstu.alamgir.server.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateDemandRequest {

    @NotBlank(message = "Location ID is required")
    private String locationId;

    @NotBlank(message = "Product ID is required")
    private String productId;

    @NotNull(message = "Date is required")
    private String date;

    @NotNull(message = "Min quantity is required")
    private Integer minQuantity;

    @NotNull(message = "Max quantity is required")
    private Integer maxQuantity;
}

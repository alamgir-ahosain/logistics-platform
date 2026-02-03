package csembstu.alamgir.server.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DateRequestValidation {

    @NotBlank(message = "capacity  is required")
    private String date;
}

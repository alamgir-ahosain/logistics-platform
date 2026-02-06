package csembstu.alamgir.server.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DateRequestValidation {

    @NotBlank(message = "capacity  is required")
    private String date;
}

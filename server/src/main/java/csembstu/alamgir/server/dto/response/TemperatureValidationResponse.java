package csembstu.alamgir.server.dto.response;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TemperatureValidationResponse {
    private boolean valid;
    private List<String> issues;

    public TemperatureValidationResponse(boolean valid, List<String> issues) {
        this.valid = valid;
        this.issues = issues;
    }

}

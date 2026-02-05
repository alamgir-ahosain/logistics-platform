package csembstu.alamgir.server.dto.response;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class FullLogisticsValidationResponse {
    private List<String> temperature_incompatible_demands;
    private List<CapacityViolation> capacity_violations;
    private List<StorageCapacityViolation> storage_capacity_violations;

    public FullLogisticsValidationResponse() {
        this.temperature_incompatible_demands = new ArrayList<>();
        this.capacity_violations = new ArrayList<>();
        this.storage_capacity_violations = new ArrayList<>();
    }
}

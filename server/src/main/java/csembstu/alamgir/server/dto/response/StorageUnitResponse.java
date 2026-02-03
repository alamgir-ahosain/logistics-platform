package csembstu.alamgir.server.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class StorageUnitResponse {

    private String id;
    private String locationId;
    private double minTemperature;
    private double maxTemperature;
    private int capacity;
}

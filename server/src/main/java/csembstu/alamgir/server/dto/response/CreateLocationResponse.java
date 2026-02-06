package csembstu.alamgir.server.dto.response;

import csembstu.alamgir.server.dto.LocationType;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateLocationResponse {

    private String id;
    private String name;
    private LocationType type; // PRODUCER, WAREHOUSE, RETAILER, HOSPITAL
    private String city;

}

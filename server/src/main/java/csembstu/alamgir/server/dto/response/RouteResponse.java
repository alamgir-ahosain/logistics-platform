package csembstu.alamgir.server.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RouteResponse {

    private String id;
    private String fromLocationId;
    private String toLocationId;
    private int capacity;
    private int minShipment;

}

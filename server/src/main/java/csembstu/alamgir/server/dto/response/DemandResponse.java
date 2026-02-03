package csembstu.alamgir.server.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DemandResponse {

    private String id;
    private String locationId;
    private String productId;
    private String date;
    private int minQuantity;
    private int maxQuantity;
}

package csembstu.alamgir.server.dto.response;

import csembstu.alamgir.server.entity.Location;
import csembstu.alamgir.server.entity.Product;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LogisticsSummaryResponse {
    private List<Location> locations;
    private List<Product> products;
    private List<StorageUnitResponse> storageUnits; // Use DTOs here!
    private List<RouteResponse> routes;
    private List<DemandResponse> demands;
}
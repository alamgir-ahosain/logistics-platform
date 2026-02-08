package csembstu.alamgir.server.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class StorageCapacityViolation {

    private String warehouse;
    private int total_required_capacity;
    private int storage_capacity;

    public StorageCapacityViolation(String warehouse, int total_required_capacity, int storage_capacity) {
        this.warehouse = warehouse;
        this.total_required_capacity = total_required_capacity;
        this.storage_capacity = storage_capacity;
    }

}
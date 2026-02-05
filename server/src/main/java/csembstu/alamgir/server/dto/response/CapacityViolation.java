package csembstu.alamgir.server.dto.response;

import lombok.Data;

@Data
public class CapacityViolation {
    private String violation;
    private String from;
    private String to;
    private int used_capacity; // demand maxQuantity
    private int capacity; // route capacity

    public CapacityViolation(String violation, String from, String to, int used_capacity, int capacity) {
        this.violation = violation;
        this.from = from;
        this.to = to;
        this.used_capacity = used_capacity;
        this.capacity = capacity;
    }

}
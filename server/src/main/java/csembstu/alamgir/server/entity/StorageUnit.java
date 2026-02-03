package csembstu.alamgir.server.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Entity
@Table(name = "storage_units")
@Data
@Accessors(chain = true)
public class StorageUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location; // Must be of type WAREHOUSE

    private double minTemperature;
    private double maxTemperature;
    private int capacity;
}
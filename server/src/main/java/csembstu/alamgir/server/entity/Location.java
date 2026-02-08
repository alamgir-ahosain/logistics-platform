package csembstu.alamgir.server.entity;

import csembstu.alamgir.server.dto.LocationType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Entity
@Table(name = "locations")
@Accessors(chain = true)

public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    @Enumerated(EnumType.STRING)
    private LocationType type; // PRODUCER, WAREHOUSE, RETAILER, HOSPITAL

    private String city;
}
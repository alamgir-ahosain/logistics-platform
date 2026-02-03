package csembstu.alamgir.server.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Entity
@Table(name = "demands")
@Data
@Accessors(chain = true)
public class Demand {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "location_id")
    public Location location;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private String date;
    private int minQuantity;
    private int maxQuantity;
}

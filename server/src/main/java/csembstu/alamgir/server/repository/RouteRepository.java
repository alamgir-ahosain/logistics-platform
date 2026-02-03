package csembstu.alamgir.server.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import csembstu.alamgir.server.entity.Route;


@Repository
public interface RouteRepository extends JpaRepository<Route, String> {

    List<Route> findByToLocation_Id(String toLocationId);
    List<Route> findByFromLocation_Id(String warehouseId);

}

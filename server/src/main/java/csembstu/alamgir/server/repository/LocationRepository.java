package csembstu.alamgir.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import csembstu.alamgir.server.entity.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, String> {

    boolean existsByNameAndCity(String name, String city);

}

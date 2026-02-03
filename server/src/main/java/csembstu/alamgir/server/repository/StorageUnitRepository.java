package csembstu.alamgir.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import csembstu.alamgir.server.entity.StorageUnit;


@Repository
public interface StorageUnitRepository extends JpaRepository<StorageUnit, String> {
    List<StorageUnit> findByLocation_Id(String locationId);

}

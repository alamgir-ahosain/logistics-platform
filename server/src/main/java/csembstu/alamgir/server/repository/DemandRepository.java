package csembstu.alamgir.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import csembstu.alamgir.server.entity.Demand;


@Repository
public interface DemandRepository extends JpaRepository<Demand, String> {

    List<Demand> findByDate(String date);

}

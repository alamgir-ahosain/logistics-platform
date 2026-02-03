package csembstu.alamgir.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import csembstu.alamgir.server.entity.Product;


@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    boolean existsByName(String name);

}
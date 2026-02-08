package csembstu.alamgir.server.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import csembstu.alamgir.server.entity.Product;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void init() {
        Product mockProduct = new Product();
        mockProduct.setName("Frozen Vaccine");
        mockProduct.setMaxTemperature(-20);
        mockProduct.setMinTemperature(-10);
        productRepository.save(mockProduct);
    }


    
    @Test
    @DisplayName("EXISTS: Should return true when name  match")
    void shouldReturnTrueWhenNameMatch() {

        boolean exists = productRepository.existsByName("Frozen Vaccine");
        assertTrue(exists);
    }




    @Test
    @DisplayName("EXISTS: Should return false when Name does not match")
    void shouldReturnFalseWhenNameIsIncorrect() {
        boolean exists = productRepository.existsByName("Alamgir");
        assertFalse(exists);
    }
}

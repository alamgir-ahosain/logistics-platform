package csembstu.alamgir.server.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import csembstu.alamgir.server.dto.request.CreateProductRequest;
import csembstu.alamgir.server.entity.Product;
import csembstu.alamgir.server.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
     ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private CreateProductRequest newProduct1;
    private CreateProductRequest newProduct2;

    @BeforeEach
    void init() {
        newProduct1 = new CreateProductRequest();    newProduct1.setName("Frozen Vaccine");   newProduct1.setMaxTemperature(-20.0);     newProduct1.setMinTemperature(-10.0);
        newProduct2 = new CreateProductRequest();    newProduct2.setName("Fresh Milk");   newProduct2.setMaxTemperature(2.0);   newProduct2.setMinTemperature(6.0);
    }

    private Product mapToEntity(CreateProductRequest request, String id) {
        Product product = new Product();  product.setId(id);    product.setName(request.getName());    product.setMaxTemperature(request.getMaxTemperature());    product.setMinTemperature(request.getMinTemperature());
        return product;
    }




    @Test
    @DisplayName("Create Product: Should save successfully when product is unique")
    void shouldSaveProductSuccessfullyWhenUnique() throws BadRequestException {

        // GIVEN
        Product mockProduct1 = mapToEntity(newProduct1, "p1");
        when(productRepository.existsByName(mockProduct1.getName())).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(mockProduct1);

        // WHEN
        Product savedProduct = productService.createProduct(newProduct1);

        // THEN
        assertNotNull(savedProduct);
        assertEquals("Frozen Vaccine", savedProduct.getName());
        assertEquals(-20.0, savedProduct.getMaxTemperature());
        assertEquals(-10.0, savedProduct.getMinTemperature());

        // VERIFY
        verify(productRepository).existsByName("Frozen Vaccine");
        verify(productRepository, times(1)).save(any(Product.class));

    }



    @Test
    @DisplayName("Create Product: Should throw BadRequestException when Product exists")
    void shouldThrowExceptionWhenProductAlreadyExists() {

        // GIVEN
        Product mockProduct = mapToEntity(newProduct1, "p1");
        when(productRepository.existsByName(mockProduct.getName())).thenReturn(true);

        // WHEN
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            productService.createProduct(newProduct1);
        });

        // THEN
        assertTrue(exception.getMessage().contains("already exists"));

    }




    @Test
    @DisplayName("Get All Product: Should return list of all products")
    void shouldReturnAllProductSuccessfully() {
        
        // GIVEN
        Product mockProduct1 = mapToEntity(newProduct1, "p1");
        Product mockProduct2 = mapToEntity(newProduct2, "p2");
        List<Product> productList = Arrays.asList(mockProduct1, mockProduct2);
        when(productRepository.findAll()).thenReturn(productList);

        // WHEN
        List<Product> products = productService.getAllProduct();

        // THEN
        assertNotNull(products);
        assertEquals(2, products.size());
        assertEquals(mockProduct1, products.get(0));
        assertEquals(mockProduct2, products.get(1));

        // VERIFY
        verify(productRepository, times(1)).findAll();
    }
}

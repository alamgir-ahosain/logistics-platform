package csembstu.alamgir.server.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import csembstu.alamgir.server.dto.request.CreateProductRequest;
import csembstu.alamgir.server.entity.Product;
import csembstu.alamgir.server.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired   private MockMvc mockMvc;
    @MockitoBean  private ProductService productService;
    @Autowired  private ObjectMapper objectMapper;

    private CreateProductRequest newProduct1;
    private CreateProductRequest newProduct2;

    @BeforeEach
    void init() {
        newProduct1 = new CreateProductRequest();    newProduct1.setName("Frozen Vaccine");    newProduct1.setMaxTemperature(-20.0);    newProduct1.setMinTemperature(-10.0);
        newProduct2 = new CreateProductRequest();    newProduct2.setName("Fresh Milk");    newProduct2.setMaxTemperature(2.0);    newProduct2.setMinTemperature(6.0);
    }

    private Product mapToEntity(CreateProductRequest request, String id) {
        Product product = new Product();
        product.setId(id);   product.setName(request.getName());   product.setMaxTemperature(request.getMaxTemperature());   product.setMinTemperature(request.getMinTemperature());
        return product;
    }





    @Test
    @DisplayName("POST /api/products :  201 CREATED")

    void shouldCreateProductSuccessfully() throws Exception {

        // GIVEN
        Product saveProduct1 = mapToEntity(newProduct1, "p1");
        Mockito.when(productService.createProduct(Mockito.any(CreateProductRequest.class))).thenReturn(saveProduct1);

        // WHEN
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newProduct1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("p1"))
                .andExpect(jsonPath("$.maxTemperature").value(-20.0))
                .andExpect(jsonPath("$.minTemperature").value(-10.0));

        // VERIFY
        Mockito.verify(productService).createProduct(Mockito.any(CreateProductRequest.class));
    }




    @Test
    @DisplayName("GET /api/products :  200 OK")
    void shouldReturnAllProduct() throws Exception {

        // GIVEN
        Product savedProoduct1 = mapToEntity(newProduct1, "p1");
        Product savedProoduct2 = mapToEntity(newProduct2, "p1");
        List<Product> productList = Arrays.asList(savedProoduct1, savedProoduct2);

        // WHEN AND THEN
        Mockito.when(productService.getAllProduct()).thenReturn(productList);

        
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))

                .andExpect(jsonPath("$[0].name").value("Frozen Vaccine"))
                .andExpect(jsonPath("$.[0]maxTemperature").value(-20.0))
                .andExpect(jsonPath("$[0].minTemperature").value(-10.0))

                .andExpect(jsonPath("$[1].name").value("Fresh Milk"))
                .andExpect(jsonPath("$.[1]maxTemperature").value(2.0))
                .andExpect(jsonPath("$[1].minTemperature").value(6.0));

        // VERIFY
        Mockito.verify(productService).getAllProduct();
    }
}

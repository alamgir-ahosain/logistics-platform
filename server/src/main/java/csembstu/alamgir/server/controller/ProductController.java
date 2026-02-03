package csembstu.alamgir.server.controller;

import org.springframework.web.bind.annotation.RestController;

import csembstu.alamgir.server.dto.request.CreateProductRequest;
import csembstu.alamgir.server.entity.Product;
import csembstu.alamgir.server.service.ProductService;
import jakarta.validation.Valid;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/api/products")
    public ResponseEntity<Product> createProduct(@Valid @RequestBody CreateProductRequest request) throws BadRequestException {
        Product newProduct = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct); // 201 Created
    }

    @GetMapping("/api/products")
    public List<Product> getAllProduct() {return productService.getAllProduct();}
}

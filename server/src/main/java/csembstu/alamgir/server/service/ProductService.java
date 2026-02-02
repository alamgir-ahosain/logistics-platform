package csembstu.alamgir.server.service;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import csembstu.alamgir.server.dto.request.CreateProductRequest;
import csembstu.alamgir.server.entity.Product;
import csembstu.alamgir.server.repository.ProductRepository;


@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product createProduct(CreateProductRequest request) throws BadRequestException {

        boolean existsByName =productRepository.existsByName(request.getName());
        if (existsByName)  throw new BadRequestException("Product '" + request.getName() + "' already exists ");

        Product newProduct = new Product();
        newProduct.setName(request.getName());
        newProduct.setMinTemperature(request.getMinTemperature());
        newProduct.setMaxTemperature(request.getMaxTemperature());

        productRepository.save(newProduct);
        return newProduct;

    }

    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

}

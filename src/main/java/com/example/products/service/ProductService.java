package com.example.products.service;

import java.util.List;
import com.example.products.entity.Product;
import com.example.products.exception.ProductNotFoundException;
import com.example.products.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> findAll() { return repository.findAll(); }

    public Product findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    public Product create(Product product) {
        product.setId(null);
        return repository.save(product);
    }

    public Product update(Long id, Product input) {
        Product product = findById(id);
        product.setName(input.getName());
        product.setPrice(input.getPrice());
        product.setQuantity(input.getQuantity());
        return repository.save(product);
    }

    public void delete(Long id) {
        Product product = findById(id);
        repository.delete(product);
    }
}

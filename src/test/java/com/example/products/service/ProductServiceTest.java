package com.example.products.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import com.example.products.entity.Product;
import com.example.products.exception.ProductNotFoundException;
import com.example.products.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ProductServiceTest {
    @Mock private ProductRepository repository;
    private ProductService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new ProductService(repository);
    }

    @Test
    void createIgnoresProvidedId() {
        Product input = product();
        input.setId(99L);
        when(repository.save(any(Product.class))).thenAnswer(call -> {
            Product saved = call.getArgument(0);
            assertEquals(null, saved.getId());
            saved.setId(1L);
            return saved;
        });
        assertEquals(1L, service.create(input).getId());
    }

    @Test
    void updateChangesExistingProduct() {
        Product existing = product();
        existing.setId(1L);
        Product changes = product();
        changes.setName("Nouveau");
        changes.setPrice(new BigDecimal("12.50"));
        changes.setQuantity(3);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);
        Product updated = service.update(1L, changes);
        assertEquals("Nouveau", updated.getName());
        assertEquals(new BigDecimal("12.50"), updated.getPrice());
        assertEquals(3, updated.getQuantity());
    }

    @Test
    void missingProductThrowsForReadAndDelete() {
        when(repository.findById(42L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> service.findById(42L));
        assertThrows(ProductNotFoundException.class, () -> service.delete(42L));
    }

    @Test
    void deleteExistingProduct() {
        Product existing = product();
        existing.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        service.delete(1L);
        verify(repository).delete(existing);
    }

    private Product product() {
        Product product = new Product();
        product.setName("Stylo");
        product.setPrice(new BigDecimal("2.50"));
        product.setQuantity(10);
        return product;
    }
}

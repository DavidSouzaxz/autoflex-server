package com.example.demo.controller;

import com.example.demo.dto.productionSuggestionDTO.ProductionSuggestionDTO;
import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.ProductService;


import java.util.List;

@RestController
@RequestMapping("/api/production")
public class ProductionController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/suggestion")
    public ResponseEntity<List<ProductionSuggestionDTO>> getSuggestion() {
        return ResponseEntity.ok(productService.calculateOptimalProduction());
    }
    @GetMapping
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @PostMapping
    public Product create(@RequestBody Product product) {
        // Garante que a relação bidirecional do JPA funcione
        if (product.getMaterials() != null) {
            product.getMaterials().forEach(m -> m.setProduct(product));
        }
        return productRepository.save(product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
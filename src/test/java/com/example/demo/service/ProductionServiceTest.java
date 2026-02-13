package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.entity.ProductMaterial;
import com.example.demo.entity.RawMaterial;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.RawMaterialRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ProductionServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private RawMaterialRepository materialRepository;

    @InjectMocks
    private ProductionService productionService;

    @Test
    @DisplayName("Should prioritize higher price products when stock is limited")
    void shouldPrioritizeHighPriceProducts() {
        // 1. Mock de um material (10 unidades)
        RawMaterial wood = new RawMaterial();
        wood.setId(1L);
        wood.setName("Wood");
        wood.setStockQuantity(10.0);


        Product expensiveTable = new Product();
        expensiveTable.setName("Expensive Table");
        expensiveTable.setPrice(1000.0);
        
        ProductMaterial pm1 = new ProductMaterial();
        pm1.setMaterial(wood);
        pm1.setQuantityNeeded(8.0);
        expensiveTable.setMaterials(List.of(pm1));


        Product cheapChair = new Product();
        cheapChair.setName("Cheap Chair");
        cheapChair.setPrice(100.0);
        
        ProductMaterial pm2 = new ProductMaterial();
        pm2.setMaterial(wood);
        pm2.setQuantityNeeded(4.0);
        cheapChair.setMaterials(List.of(pm2));


        when(productRepository.findAllByOrderByPriceDesc()).thenReturn(List.of(expensiveTable, cheapChair));
        when(materialRepository.findAll()).thenReturn(List.of(wood));


        var result = productionService.calculateOptimalProduction();


        assertEquals(1, result.size());
        assertEquals("Expensive Table", result.get(0).getProductName());

    }
}
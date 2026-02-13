package com.example.demo.service;

import com.example.demo.dto.productionSuggestionDTO.ProductionSuggestionDTO;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductMaterial;
import com.example.demo.entity.RawMaterial;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.RawMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductionService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RawMaterialRepository materialRepository;

    public List<ProductionSuggestionDTO> calculateOptimalProduction() {

        List<Product> products = productRepository.findAllByOrderByPriceDesc();


        Map<Long, Double> tempStock = materialRepository.findAll().stream()
                .collect(Collectors.toMap(RawMaterial::getId, RawMaterial::getStockQuantity));

        List<ProductionSuggestionDTO> suggestions = new ArrayList<>();

        for (Product product : products) {

            int quantityPossible = calculateMaxPossible(product, tempStock);

            if (quantityPossible > 0) {

                updateTempStock(product, quantityPossible, tempStock);
                
                suggestions.add(new ProductionSuggestionDTO(
                        product.getName(),
                        quantityPossible,
                        product.getPrice(),
                        quantityPossible * product.getPrice()
                ));
            }
        }
        return suggestions;
    }

    private int calculateMaxPossible(Product product, Map<Long, Double> tempStock) {
        int maxQuantity = Integer.MAX_VALUE;

        if (product.getMaterials() == null || product.getMaterials().isEmpty()) {
            return 0;
        }

        for (ProductMaterial pm : product.getMaterials()) {
            Double available = tempStock.getOrDefault(pm.getMaterial().getId(), 0.0);
            
            // Se um material necessário não tem estoque, não pode produzir nada
            if (available <= 0 || pm.getQuantityNeeded() <= 0) {
                return 0;
            }

            int possibleWithThisMaterial = (int) (available / pm.getQuantityNeeded());
            
            // O limite de produção é definido pelo material mais escasso
            if (possibleWithThisMaterial < maxQuantity) {
                maxQuantity = possibleWithThisMaterial;
            }
        }
        return maxQuantity;
    }

    private void updateTempStock(Product product, int quantity, Map<Long, Double> tempStock) {
        for (ProductMaterial pm : product.getMaterials()) {
            Long materialId = pm.getMaterial().getId();
            Double usedAmount = pm.getQuantityNeeded() * quantity;
            Double currentStock = tempStock.get(materialId);
            tempStock.put(materialId, currentStock - usedAmount);
        }
    }
}
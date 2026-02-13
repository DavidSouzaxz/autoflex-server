package com.example.demo.dto.productionSuggestionDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductionSuggestionDTO {
    private String productName;
    private Integer quantityToProduce;
    private Double unitPrice;
    private Double totalValue;
}
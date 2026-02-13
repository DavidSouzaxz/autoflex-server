package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "product_materials")
@Data
public class ProductMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnoreProperties("materials")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "material_id")
    private RawMaterial material;

    @Column(nullable = false)
    private Double quantityNeeded;
}
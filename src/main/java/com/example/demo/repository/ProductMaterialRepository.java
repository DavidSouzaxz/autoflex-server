package com.example.demo.repository;

import com.example.demo.entity.ProductMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductMaterialRepository extends JpaRepository<ProductMaterial, Long> {
}
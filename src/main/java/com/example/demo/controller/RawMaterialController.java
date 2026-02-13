package com.example.demo.controller;
import com.example.demo.entity.RawMaterial;
import com.example.demo.repository.RawMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materials")
public class RawMaterialController {

    @Autowired
    private RawMaterialRepository repository;

    @GetMapping
    public List<RawMaterial> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public RawMaterial create(@RequestBody RawMaterial material) {
        return repository.save(material);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RawMaterial> update(@PathVariable Long id, @RequestBody RawMaterial details) {
        return repository.findById(id).map(material -> {
            material.setName(details.getName());
            material.setStockQuantity(details.getStockQuantity());
            return ResponseEntity.ok(repository.save(material));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
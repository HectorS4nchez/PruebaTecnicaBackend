package com.example.pruebatecnicabackend.infrastructure.rest.controller;

import com.example.pruebatecnicabackend.application.impl.PropertyService;
import com.example.pruebatecnicabackend.domain.model.PropertyModel;
import com.example.pruebatecnicabackend.infrastructure.rest.dto.PropertyResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @PostMapping
    public ResponseEntity<PropertyModel> createProperty(@RequestBody PropertyModel property) {
        PropertyModel savedProperty = propertyService.createProperty(property);
        return ResponseEntity.ok(savedProperty);
    }

    @GetMapping
    public ResponseEntity<PropertyResponse> getProperties(@RequestParam double minPrice, @RequestParam double maxPrice) {
        PropertyResponse response = propertyService.getProperties(minPrice, maxPrice);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PropertyModel> updateProperty(@PathVariable Long id, @RequestBody PropertyModel property) {
        property.setId(id);
        PropertyModel updatedProperty = propertyService.updateProperty(property);
        return ResponseEntity.ok(updatedProperty);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProperty(@PathVariable Long id) {
        propertyService.deleteProperty(id);
        return ResponseEntity.ok("Property deleted (under logic) successfully.");
    }

    @PostMapping("/{id}/rent")
    public ResponseEntity<String> rentProperty(@PathVariable Long id) {
        propertyService.rentProperty(id);
        return ResponseEntity.ok("Property leased successfully.");
    }

}

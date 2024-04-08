package com.example.pruebatecnicabackend.infrastructure.rest.controller;

import com.example.pruebatecnicabackend.application.service.PropertyService;
import com.example.pruebatecnicabackend.domain.model.PropertyModel;
import com.example.pruebatecnicabackend.infrastructure.rest.dto.response.ListPropertyResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<ListPropertyResponse> getProperties(@RequestParam double minPrice, @RequestParam double maxPrice) {
        ListPropertyResponse response = propertyService.getProperties(minPrice, maxPrice);
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

    @GetMapping("/all")
    public ResponseEntity<List<PropertyModel>> getAllProperties() {
        List<PropertyModel> properties = propertyService.findAllProperties();
        return ResponseEntity.ok(properties);
    }

}

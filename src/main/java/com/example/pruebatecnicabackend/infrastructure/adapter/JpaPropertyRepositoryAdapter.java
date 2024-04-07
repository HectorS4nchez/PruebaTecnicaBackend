package com.example.pruebatecnicabackend.infrastructure.adapter;

import com.example.pruebatecnicabackend.domain.model.PropertyModel;
import com.example.pruebatecnicabackend.domain.port.PropertyPort;
import com.example.pruebatecnicabackend.infrastructure.adapter.SQL.repository.IJpaPropertyRepositoryAdapter;
import com.example.pruebatecnicabackend.infrastructure.entities.PropertyEntity;
import com.example.pruebatecnicabackend.infrastructure.rest.dto.PropertyResponse;
import com.example.pruebatecnicabackend.infrastructure.rest.mapper.PropertyMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JpaPropertyRepositoryAdapter implements PropertyPort {

    private final IJpaPropertyRepositoryAdapter repositoryAdapter;
    private final Logger logger = LoggerFactory.getLogger(JpaPropertyRepositoryAdapter.class);


    @Override
    public PropertyModel createProperty(PropertyModel property) {
        logger.debug("Attempting to register a new property: {}", property);

        validatePropertyFields(property);
        validateUniqueName(property.getName());
        validateLocation(property.getLocation());
        validatePriceByLocation(property.getLocation(), property.getPrice());

        try {
            PropertyEntity propertyEntity = PropertyMapper.modelToEntity(property);
            PropertyEntity savedEntity = repositoryAdapter.save(propertyEntity);
            logger.info("Successfully created property with ID: {}", savedEntity.getId());
            return PropertyMapper.entityToModel(savedEntity);
        } catch (Exception e) {
            logger.error("Error while creating property: {}", property, e);
            throw e;
        }
    }

    @Override
    public PropertyResponse getProperties(double minPrice, double maxPrice) {
        List<PropertyEntity> entities = repositoryAdapter.findByPriceBetweenAndAvailabilityTrue(minPrice, maxPrice);
        List<PropertyModel> models = entities.stream()
                .map(PropertyMapper::entityToModel)
                .collect(Collectors.toList());
        return new PropertyResponse(models, models.isEmpty() ? "No properties found that meet the criteria." : "The request was successful.");
    }



    public PropertyModel updateProperty(PropertyModel updatedProperty) {
        PropertyEntity existingEntity = repositoryAdapter.findById(updatedProperty.getId())
                .orElseThrow(() -> new IllegalArgumentException("The property does not exist"));

        PropertyModel existingProperty = PropertyMapper.entityToModel(existingEntity);

        validatePropertyFields(updatedProperty);

        if (!existingProperty.isAvailability()) {
            if (!existingProperty.getLocation().equals(updatedProperty.getLocation())) {
                throw new IllegalArgumentException("Cannot modify the location of a leased property");
            }
            if (existingProperty.getPrice() != updatedProperty.getPrice()) {
                throw new IllegalArgumentException("Cannot modify the price of a leased property");
            }
        }

        validatePriceByLocation(updatedProperty.getLocation(), updatedProperty.getPrice());
        existingProperty.setName(updatedProperty.getName());
        existingProperty.setAvailability(updatedProperty.isAvailability());
        existingProperty.setImageUrl(updatedProperty.getImageUrl());
        existingProperty.setLocation(updatedProperty.getLocation());
        existingProperty.setPrice(updatedProperty.getPrice());
        PropertyEntity updatedEntity = PropertyMapper.modelToEntity(existingProperty);
        updatedEntity = repositoryAdapter.save(updatedEntity);
        return PropertyMapper.entityToModel(updatedEntity);
    }


    @Override
    public void deleteProperty(Long id) {
        PropertyEntity entity = repositoryAdapter.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Property not found with ID: " + id));

        long ageInDays = ChronoUnit.DAYS.between(entity.getCreatedAt(), LocalDateTime.now());
        if (ageInDays > 30) {
            throw new IllegalArgumentException("You can only delete properties less than a month old");
        }

        entity.setAvailability(false);
        repositoryAdapter.save(entity);
    }

    @Override
    public void rentProperty(Long propertyId) {
        PropertyEntity property = repositoryAdapter.findById(propertyId)
                .orElseThrow(() -> new IllegalArgumentException("Property not found with ID: " + propertyId));

        if (!property.isAvailability()) {
            throw new IllegalArgumentException("The property is no longer available for rent.");
        }

        property.setAvailability(false);
        repositoryAdapter.save(property);
    }


    private void validatePropertyFields(PropertyModel property) {
        if (property.getName() == null || property.getName().trim().isEmpty() ||
                property.getLocation() == null || property.getLocation().trim().isEmpty() ||
                property.getImageUrl() == null || property.getImageUrl().trim().isEmpty() ||
                property.getPrice() <= 0) {
            throw new IllegalArgumentException("Property fields must not be empty and price must be greater than 0");
        }
    }

    private void validateUniqueName(String name) {
        if (repositoryAdapter.findByName(name) == null) {
            throw new IllegalArgumentException("A property with the same name already exists");
        }
    }

    private void validateLocation(String location) {
        List<String> allowedLocations = Arrays.asList("Medellin", "Bogota", "Cali", "Cartagena");
        if (!allowedLocations.contains(location)) {
            throw new IllegalArgumentException("Property location is not valid");
        }
    }

    private void validatePriceByLocation(String location, double price) {
        if (("Bogota".equals(location) || "Cali".equals(location)) && price <= 2000000) {
            throw new IllegalArgumentException("Properties in Bogotá and Cali must have a price greater than 2,000,000");
        }
    }
}

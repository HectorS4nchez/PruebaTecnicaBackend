package com.example.pruebatecnicabackend.infrastructure.adapter;

import com.example.pruebatecnicabackend.domain.model.PropertyModel;
import com.example.pruebatecnicabackend.domain.port.PropertyPort;
import com.example.pruebatecnicabackend.infrastructure.adapter.repository.IJpaPropertyRepositoryAdapter;
import com.example.pruebatecnicabackend.infrastructure.adapter.entities.PropertyEntity;
import com.example.pruebatecnicabackend.infrastructure.rest.dto.response.ListPropertyResponse;
import com.example.pruebatecnicabackend.infrastructure.rest.exceptions.CustomIllegalArgumentException;
import com.example.pruebatecnicabackend.infrastructure.rest.exceptions.ErrorCode;
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

        try {
            validatePropertyFields(property);
            validateUniqueName(property.getName());
            validateLocation(property.getLocation());
            validatePriceByLocation(property.getLocation(), property.getPrice());

            PropertyEntity propertyEntity = PropertyMapper.modelToEntity(property);
            PropertyEntity savedEntity = repositoryAdapter.save(propertyEntity);
            logger.info("Successfully created property with ID: {}", savedEntity.getId());
            return PropertyMapper.entityToModel(savedEntity);
        } catch (CustomIllegalArgumentException e) {
            logger.error("Error creating property: {}", e.getErrorCode().getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error creating property: {}", e.getMessage());
            throw new CustomIllegalArgumentException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    @Override
    public ListPropertyResponse getProperties(double minPrice, double maxPrice) {
        logger.info("Getting properties with price between {} and {}", minPrice, maxPrice);
        try {
            List<PropertyEntity> entities = repositoryAdapter.findByPriceBetweenAndAvailabilityTrue(minPrice, maxPrice);
            if (entities.isEmpty()) {
                logger.warn("No properties found that meet the search criteria");
            } else {
                logger.info("Found {} properties that meet criteria", entities.size());
            }
            List<PropertyModel> models = entities.stream()
                    .map(PropertyMapper::entityToModel)
                    .collect(Collectors.toList());
            return new ListPropertyResponse(models, models.isEmpty() ? ErrorCode.INVALID_PROPERTY_CRITERIA.getMessage() : ErrorCode.REQUEST_SUCCESSFUL.getMessage());
        } catch (Exception e) {
            logger.error("Error getting properties", e);
            throw e;
        }
    }

    @Override
    public PropertyModel updateProperty(PropertyModel updatedProperty) {
        try {
            PropertyEntity existingEntity = repositoryAdapter.findById(updatedProperty.getId())
                    .orElseThrow(() -> new CustomIllegalArgumentException(ErrorCode.PROPERTY_NOT_FOUND));

            PropertyModel existingProperty = PropertyMapper.entityToModel(existingEntity);
            logger.debug("Property details before update: {}", existingEntity);

            validatePropertyFields(updatedProperty);

            if (!existingProperty.isAvailability()) {
                logger.warn("Attempt to modify property not available for rent, ID: {}", existingEntity.getId());
                if (!existingProperty.getLocation().equals(updatedProperty.getLocation())) {
                    throw new CustomIllegalArgumentException(ErrorCode.CANNOT_MODIFY_LEASED_PROPERTY_LOCATION);
                }
                if (existingProperty.getPrice() != updatedProperty.getPrice()) {
                    throw new CustomIllegalArgumentException(ErrorCode.CANNOT_MODIFY_LEASED_PROPERTY_PRICE);
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
            logger.info("Property with ID: {} updated successfully", updatedProperty.getId());
            return PropertyMapper.entityToModel(updatedEntity);
        } catch (CustomIllegalArgumentException e) {
            logger.error("Error updating property: {}", e.getErrorCode().getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error updating property with ID: {}", updatedProperty.getId(), e);
            throw e;
        }
    }

    @Override
    public void deleteProperty(Long id) {
        try {
            logger.info("Trying to delete property with ID: {}", id);
            PropertyEntity entity = repositoryAdapter.findById(id)
                    .orElseThrow(() -> new CustomIllegalArgumentException(ErrorCode.PROPERTY_NOT_FOUND));

            long ageInDays = ChronoUnit.DAYS.between(entity.getCreatedAt(), LocalDateTime.now());
            if (ageInDays > 30) {
                logger.warn("Property with ID: {} is too old to be deleted", id);
                throw new CustomIllegalArgumentException(ErrorCode.PROPERTY_TOO_OLD_TO_DELETE);
            }

            entity.setAvailability(false);
            repositoryAdapter.save(entity);
            logger.info("Property with ID: {} logically deleted successfully", id);
        } catch (CustomIllegalArgumentException e) {
            logger.error("Error deleting property: {}", e.getErrorCode().getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error deleting property with ID: {}", id, e);
            throw e;
        }
    }

    @Override
    public void rentProperty(Long propertyId) {
        logger.info("Trying to rent property with ID: {}", propertyId);
        try {
            PropertyEntity property = repositoryAdapter.findById(propertyId)
                    .orElseThrow(() -> new CustomIllegalArgumentException(ErrorCode.PROPERTY_NOT_FOUND));

            if (!property.isAvailability()) {
                logger.warn("Property with ID: {} is no longer available for rent", propertyId);
                throw new CustomIllegalArgumentException(ErrorCode.PROPERTY_NOT_AVAILABLE_FOR_RENT);
            }

            property.setAvailability(false);
            repositoryAdapter.save(property);
            logger.info("Property with ID: {} rented successfully", propertyId);
        } catch (CustomIllegalArgumentException e) {
            logger.error("Error renting property: {}", e.getErrorCode().getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error renting property with ID: {}", propertyId, e);
            throw e;
        }
    }


    @Override
    public List<PropertyModel> findAllProperties() {
        logger.info("Starting search for all available properties.");
        try {
            List<PropertyEntity> entities = (List<PropertyEntity>) repositoryAdapter.findAll();

            if (entities.isEmpty()) {
                logger.info("No available properties found.");
            } else {
                logger.info("{} available properties found.", entities.size());
            }

            return entities.stream()
                    .map(PropertyMapper::entityToModel)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Unexpected error while trying to retrieve all properties.", e);
            throw e;
        }
    }

    private void validatePropertyFields(PropertyModel property) {
        logger.debug("Validating property fields: {}", property);
        if (property.getName() == null || property.getName().trim().isEmpty() ||
                property.getLocation() == null || property.getLocation().trim().isEmpty() ||
                property.getImageUrl() == null || property.getImageUrl().trim().isEmpty() ||
                property.getPrice() <= 0) {
            logger.warn("Property fields are invalid: {}", property);
            throw new CustomIllegalArgumentException(ErrorCode.PROPERTY_FIELDS_EMPTY);
        }
    }

    private void validateUniqueName(String name) {
        logger.debug("Validating unique name for property: {}", name);
        repositoryAdapter.findByName(name).ifPresent(s -> {
            logger.warn("The property name already exists: {}", name);
            throw new CustomIllegalArgumentException(ErrorCode.PROPERTY_NAME_EXISTS);
        });
    }

    private void validateLocation(String location) {
        logger.debug("Validating property location: {}", location);
        List<String> allowedLocations = Arrays.asList("Medellin", "Bogota", "Cali", "Cartagena");
        if (!allowedLocations.contains(location)) {
            logger.warn("Invalid property location: {}", location);
            throw new CustomIllegalArgumentException(ErrorCode.INVALID_PROPERTY_LOCATION);
        }
    }

    private void validatePriceByLocation(String location, double price) {
        logger.debug("Validating price by location. Location: {}, Price: {}", location, price);
        if (("Bogota".equals(location) || "Cali".equals(location)) && price < 2000000) {
            logger.warn("The property price in {} is less than the minimum required: {}", location, price);
            throw new CustomIllegalArgumentException(ErrorCode.PRICE_LESS_THAN_MINIMUM);
        }
    }

}

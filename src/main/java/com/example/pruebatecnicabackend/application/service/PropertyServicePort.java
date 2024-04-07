package com.example.pruebatecnicabackend.application.service;

import com.example.pruebatecnicabackend.domain.model.PropertyModel;
import com.example.pruebatecnicabackend.infrastructure.rest.dto.PropertyResponse;

public interface PropertyServicePort {

    PropertyModel createProperty (PropertyModel property);

    PropertyResponse getProperties(double minPrice, double maxPrice);
    PropertyModel updateProperty (PropertyModel property);
    void deleteProperty (Long id);

    void rentProperty(Long propertyId);

}

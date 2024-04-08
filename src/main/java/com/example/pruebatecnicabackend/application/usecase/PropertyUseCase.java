package com.example.pruebatecnicabackend.application.usecase;

import com.example.pruebatecnicabackend.domain.model.PropertyModel;
import com.example.pruebatecnicabackend.infrastructure.rest.dto.response.ListPropertyResponse;

import java.util.List;

public interface PropertyUseCase {

    PropertyModel createProperty (PropertyModel property);
    ListPropertyResponse getProperties(double minPrice, double maxPrice);
    PropertyModel updateProperty (PropertyModel property);
    void deleteProperty (Long id);
    void rentProperty(Long propertyId);
    List<PropertyModel> findAllProperties();

}

package com.example.pruebatecnicabackend.application.impl;

import com.example.pruebatecnicabackend.application.service.PropertyServicePort;
import com.example.pruebatecnicabackend.domain.model.PropertyModel;
import com.example.pruebatecnicabackend.domain.port.PropertyPort;
import com.example.pruebatecnicabackend.infrastructure.rest.dto.PropertyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PropertyService implements PropertyServicePort {

    private final PropertyPort propertyPort;


    @Override
    public PropertyModel createProperty(PropertyModel property) {
        return propertyPort.createProperty(property);
    }

    @Override
    public PropertyResponse getProperties(double minPrice, double maxPrice) {
        return propertyPort.getProperties(minPrice, maxPrice);
    }

    @Override
    public PropertyModel updateProperty(PropertyModel property) {
        return propertyPort.updateProperty(property);
    }

    @Override
    public void deleteProperty(Long id) {
        propertyPort.deleteProperty(id);
    }

    @Override
    public void rentProperty(Long id) {
        propertyPort.rentProperty(id);
    }
}

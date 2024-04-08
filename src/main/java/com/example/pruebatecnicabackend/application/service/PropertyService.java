package com.example.pruebatecnicabackend.application.service;

import com.example.pruebatecnicabackend.application.usecase.PropertyUseCase;
import com.example.pruebatecnicabackend.domain.model.PropertyModel;
import com.example.pruebatecnicabackend.domain.port.PropertyPort;
import com.example.pruebatecnicabackend.infrastructure.rest.dto.response.ListPropertyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyService implements PropertyUseCase {

    private final PropertyPort propertyPort;

    @Override
    public PropertyModel createProperty(PropertyModel property) {
        return propertyPort.createProperty(property);
    }

    @Override
    public ListPropertyResponse getProperties(double minPrice, double maxPrice) {
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

    @Override
    public List<PropertyModel> findAllProperties() {
        return propertyPort.findAllProperties();
    }
}

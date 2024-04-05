package com.example.pruebatecnicabackend.application.impl;

import com.example.pruebatecnicabackend.application.service.PropertyServicePort;
import com.example.pruebatecnicabackend.domain.model.PropertyModel;
import com.example.pruebatecnicabackend.domain.port.PropertyPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PropertyService implements PropertyServicePort {

    private PropertyPort propertyPort;

    public PropertyService(PropertyPort propertyPort) {
        this.propertyPort = propertyPort;
    }

    @Override
    public PropertyModel createProperty(PropertyModel property) {
        return propertyPort.createProperty(property);
    }

    @Override
    public List<PropertyModel> getProperties() {
        return propertyPort.getProperties();
    }

    @Override
    public List<PropertyModel> getProperties(double minPrice, double maxPrice) {
        return propertyPort.getProperties(minPrice,maxPrice);
    }

    @Override
    public PropertyModel updateProperty(PropertyModel property) {
        return propertyPort.updateProperty(property);
    }

    @Override
    public void deleteProperty(Long id) {
        propertyPort.deleteProperty(id);
    }
}

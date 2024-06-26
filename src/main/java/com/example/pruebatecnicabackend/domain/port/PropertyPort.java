package com.example.pruebatecnicabackend.domain.port;

import com.example.pruebatecnicabackend.domain.model.PropertyModel;
import com.example.pruebatecnicabackend.infrastructure.rest.dto.response.ListPropertyResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyPort {

    PropertyModel createProperty (PropertyModel property);
    ListPropertyResponse getProperties(double minPrice, double maxPrice);
    PropertyModel updateProperty (PropertyModel property);
    void deleteProperty (Long id);
    void rentProperty(Long id);
    List<PropertyModel> findAllProperties();

}

package com.example.pruebatecnicabackend.domain.port;

import com.example.pruebatecnicabackend.domain.model.PropertyModel;
import com.example.pruebatecnicabackend.infrastructure.rest.dto.PropertyResponse;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyPort {

    PropertyModel createProperty (PropertyModel property);

    PropertyResponse getProperties(double minPrice, double maxPrice);
    PropertyModel updateProperty (PropertyModel property);
    void deleteProperty (Long id);

    void rentProperty(Long id);

}

package com.example.pruebatecnicabackend.domain.port;

import com.example.pruebatecnicabackend.domain.model.PropertyModel;

import java.util.List;

public interface PropertyPort {

    PropertyModel createProperty (PropertyModel property);
    List<PropertyModel> getProperties ();
    List<PropertyModel> getProperties (double minPrice, double maxPrice);
    PropertyModel updateProperty (PropertyModel property);
    void deleteProperty (Long id);

}

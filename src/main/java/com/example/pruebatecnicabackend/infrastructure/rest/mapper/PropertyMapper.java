package com.example.pruebatecnicabackend.infrastructure.rest.mapper;

import com.example.pruebatecnicabackend.domain.model.PropertyModel;
import com.example.pruebatecnicabackend.infrastructure.adapter.entities.PropertyEntity;


public class PropertyMapper {

    public static PropertyEntity modelToEntity(PropertyModel propertyModel) {
        PropertyEntity propertyEntity = new PropertyEntity();
        propertyEntity.setId(propertyModel.getId());
        propertyEntity.setName(propertyModel.getName());
        propertyEntity.setLocation(propertyModel.getLocation());
        propertyEntity.setAvailability(propertyModel.isAvailability());
        propertyEntity.setImageUrl(propertyModel.getImageUrl());
        propertyEntity.setPrice(propertyModel.getPrice());
        propertyEntity.setCreatedAt(propertyModel.getCreatedAt());
        return propertyEntity;
    }

    public static PropertyModel entityToModel(PropertyEntity propertyEntity){
        PropertyModel propertyModel = new PropertyModel();
        propertyModel.setId(propertyEntity.getId());
        propertyModel.setName(propertyEntity.getName());
        propertyModel.setLocation(propertyEntity.getLocation());
        propertyModel.setAvailability(propertyEntity.isAvailability());
        propertyModel.setImageUrl(propertyEntity.getImageUrl());
        propertyModel.setPrice(propertyEntity.getPrice());
        propertyModel.setCreatedAt(propertyEntity.getCreatedAt());
        return propertyModel;
    }

}

package com.example.pruebatecnicabackend.infrastructure.rest.dto;


import com.example.pruebatecnicabackend.domain.model.PropertyModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PropertyResponse {

    private List<PropertyModel> properties;
    private String message;
}
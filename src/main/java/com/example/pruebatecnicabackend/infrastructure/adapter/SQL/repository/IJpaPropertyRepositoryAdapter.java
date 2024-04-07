package com.example.pruebatecnicabackend.infrastructure.adapter.SQL;

import com.example.pruebatecnicabackend.domain.model.PropertyModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface IJpaPropertyRepositoryAdapter extends JpaRepository<PropertyModel, Long> {

    PropertyModel findByName(String name);
    List<PropertyModel> findByPriceBetweenAndAvailabilityTrue(double minPrice, double maxPrice);
}

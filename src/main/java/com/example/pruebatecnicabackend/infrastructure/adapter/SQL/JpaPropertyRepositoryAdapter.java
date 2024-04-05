package com.example.pruebatecnicabackend.infrastructure.adapter.SQL;

import com.example.pruebatecnicabackend.domain.model.PropertyModel;
import com.example.pruebatecnicabackend.infrastructure.entities.PropertyEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface JpaPropertyRepositoryAdapter extends CrudRepository<PropertyEntity, Long> {
    List<PropertyModel> findByAvailabilityTrueAndPriceBetween(double minPrice, double maxPrice);

}

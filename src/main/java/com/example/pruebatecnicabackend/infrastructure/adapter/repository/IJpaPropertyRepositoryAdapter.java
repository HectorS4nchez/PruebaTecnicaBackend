package com.example.pruebatecnicabackend.infrastructure.adapter.repository;

import com.example.pruebatecnicabackend.infrastructure.adapter.entities.PropertyEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IJpaPropertyRepositoryAdapter extends CrudRepository<PropertyEntity, Long> {

    Optional<PropertyEntity> findByName(String name);

    List<PropertyEntity> findByPriceBetweenAndAvailabilityTrue(double minPrice, double maxPrice);
}

package com.example.pruebatecnicabackend.domain.model;

import java.time.LocalDateTime;

public class PropertyModel {

    private Long id;
    private String name;
    private String location;
    private boolean availability;
    private String imageUrl;
    private double price;
    private LocalDateTime createdAt;

    public PropertyModel() {
    }

    public PropertyModel(Long id, String name, String location, boolean availability, String imageUrl, double price) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.availability = availability;
        this.imageUrl = imageUrl;
        this.price = price;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

package com.example.pruebatecnicabackend.infrastructure.rest.controller;

import com.example.pruebatecnicabackend.application.service.PropertyService;
import com.example.pruebatecnicabackend.domain.model.PropertyModel;
import com.example.pruebatecnicabackend.infrastructure.rest.dto.response.ListPropertyResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
 class PropertyControllerTest {

    @Mock
    private PropertyService propertyService;

    @InjectMocks
    private PropertyController propertyController;

    @BeforeEach
    void setUp() {

    }

    @Test
     void test_createProperty_returnsCreatedProperty() {
        // Given
        PropertyModel property = new PropertyModel(1L, "Property 1", "Location 1", true, "image_url", 100.0);
        when(propertyService.createProperty(property)).thenReturn(property); // Mock the behavior

        // When
        ResponseEntity<PropertyModel> response = propertyController.createProperty(property);

        // Then
        assertNotNull(response.getBody());
        assertEquals(property.getId(), response.getBody().getId());
        assertEquals(property.getName(), response.getBody().getName());
        assertEquals(property.getLocation(), response.getBody().getLocation());
        assertEquals(property.isAvailability(), response.getBody().isAvailability());
        assertEquals(property.getImageUrl(), response.getBody().getImageUrl());
        assertEquals(property.getPrice(), response.getBody().getPrice(), 0.0);
    }

    @Test
     void test_return_response_with_list_property_response() {
        // Given
        double minPrice = 1000.0;
        double maxPrice = 2000.0;
        ListPropertyResponse mockResponse = new ListPropertyResponse(new ArrayList<>(), "Test response");
        when(propertyService.getProperties(minPrice, maxPrice)).thenReturn(mockResponse);

        // When
        ResponseEntity<ListPropertyResponse> response = propertyController.getProperties(minPrice, maxPrice);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test response", response.getBody().getMessage());
        assertTrue(response.getBody().getProperties().isEmpty());
    }

    @Test
     void test_updateProperty_returnsUpdatedPropertyModel() {
        // Given
        PropertyModel inputModel = new PropertyModel();
        inputModel.setId(1L);
        inputModel.setName("Test Property");
        inputModel.setLocation("Test Location");
        inputModel.setAvailability(true);
        inputModel.setImageUrl("test_image.jpg");
        inputModel.setPrice(100.0);

        // Assuming the service updates and returns the model
        when(propertyService.updateProperty(any(PropertyModel.class))).thenReturn(inputModel);

        PropertyController propertyController = new PropertyController(propertyService);

        // When
        ResponseEntity<PropertyModel> response = propertyController.updateProperty(inputModel.getId(),inputModel);
        PropertyModel updatedProperty = response.getBody();

        // Then
        assertNotNull(updatedProperty);
        assertEquals(inputModel.getId(), updatedProperty.getId());
        assertEquals(inputModel.getName(), updatedProperty.getName());
        assertEquals(inputModel.getLocation(), updatedProperty.getLocation());
        assertEquals(inputModel.isAvailability(), updatedProperty.isAvailability());
        assertEquals(inputModel.getImageUrl(), updatedProperty.getImageUrl());
        assertEquals(inputModel.getPrice(), updatedProperty.getPrice(), 0.001);
    }



    @Test
     void test_property_deleted_successfully() {
        // Given
        Long id = 1L;

        // When
        ResponseEntity<String> response = propertyController.deleteProperty(id);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Property deleted (under logic) successfully.", response.getBody());
    }

    @Test
     void test_rentPropertyWithValidIdReturnsSuccessMessage() {
        // Given
        Long id = 1L;

        // When
        ResponseEntity<String> response = propertyController.rentProperty(id);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Property leased successfully.", response.getBody());
    }

    @Test
     void test_return_all_properties_when_properties_exist() {
        // Given
        List<PropertyModel> properties = new ArrayList<>();
        properties.add(new PropertyModel(1L, "Property 1", "Location 1", true, "image1.jpg", 100.0));
        properties.add(new PropertyModel(2L, "Property 2", "Location 2", true, "image2.jpg", 200.0));
        Mockito.when(propertyService.findAllProperties()).thenReturn(properties);

        // When
        ResponseEntity<List<PropertyModel>> response = propertyController.getAllProperties();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(properties, response.getBody());
    }
}

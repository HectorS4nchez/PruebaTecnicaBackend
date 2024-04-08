package com.example.pruebatecnicabackend.infrastructure.adapter;

import com.example.pruebatecnicabackend.domain.model.PropertyModel;
import com.example.pruebatecnicabackend.infrastructure.adapter.entities.PropertyEntity;
import com.example.pruebatecnicabackend.infrastructure.adapter.repository.IJpaPropertyRepositoryAdapter;
import com.example.pruebatecnicabackend.infrastructure.rest.dto.response.ListPropertyResponse;
import com.example.pruebatecnicabackend.infrastructure.rest.mapper.PropertyMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class JpaPropertyRepositoryAdapterTest {

    @Mock
    private IJpaPropertyRepositoryAdapter repositoryAdapter;

    @InjectMocks
    private JpaPropertyRepositoryAdapter jpaPropertyRepositoryAdapter;

    @BeforeEach
    void setUp() {

    }

    @Test
     void test_successfully_creates_new_property() {
        // Given
        PropertyModel property = new PropertyModel(1L, "Property 1", "Medellin", true, "image.jpg", 2500000.0);
        PropertyEntity propertyEntity = PropertyMapper.modelToEntity(property);
        when(repositoryAdapter.save(any(PropertyEntity.class))).thenReturn(propertyEntity);

        // When
        PropertyModel result = jpaPropertyRepositoryAdapter.createProperty(property);

        // Then
        assertNotNull(result);
        assertEquals(property.getId(), result.getId());
        assertEquals(property.getName(), result.getName());
        assertEquals(property.getLocation(), result.getLocation());
        assertEquals(property.isAvailability(), result.isAvailability());
        assertEquals(property.getImageUrl(), result.getImageUrl());
        assertEquals(property.getPrice(), result.getPrice(), 0.001);
        assertNotNull(result.getCreatedAt());
    }

    @Test
     void test_throws_exception_when_property_name_is_null_or_empty() {
        // Given
        PropertyModel inputModel = new PropertyModel();
        inputModel.setName(null);
        inputModel.setLocation("Medellin");
        inputModel.setAvailability(true);
        inputModel.setImageUrl("http://example.com/image.jpg");
        inputModel.setPrice(3000000);

        // Then
        assertThrows(IllegalArgumentException.class, () -> jpaPropertyRepositoryAdapter.createProperty(inputModel));
    }

    @Test
     void test_throws_exception_when_property_location_is_null_or_empty() {
        // Given
        PropertyModel inputModel = new PropertyModel();
        inputModel.setName("Test House");
        inputModel.setLocation(null);
        inputModel.setAvailability(true);
        inputModel.setImageUrl("http://example.com/image.jpg");
        inputModel.setPrice(3000000);

        // Then
        assertThrows(IllegalArgumentException.class, () -> jpaPropertyRepositoryAdapter.createProperty(inputModel));
    }

    @Test
     void test_properties_found_within_price_range() {
        // Given
        double minPrice = 1000000;
        double maxPrice = 2000000;
        List<PropertyEntity> entities = new ArrayList<>();

        PropertyEntity entity1 = new PropertyEntity();
        entity1.setId(1L);
        entity1.setName("Property 1");
        entity1.setLocation("Medellin");
        entity1.setAvailability(true);
        entity1.setImageUrl("image1.jpg");
        entity1.setPrice(1500000);

        PropertyEntity entity2 = new PropertyEntity();
        entity2.setId(2L);
        entity2.setName("Property 2");
        entity2.setLocation("Bogota");
        entity2.setAvailability(true);
        entity2.setImageUrl("image2.jpg");
        entity2.setPrice(2000000);

        entities.add(entity1);
        entities.add(entity2);
        when(repositoryAdapter.findByPriceBetweenAndAvailabilityTrue(minPrice, maxPrice)).thenReturn(entities);

        // When
        ListPropertyResponse response = jpaPropertyRepositoryAdapter.getProperties(minPrice, maxPrice);

        // Then
        assertEquals("The request was successful.", response.getMessage());
        assertEquals(2, response.getProperties().size());
        assertEquals(entity1.getId(), response.getProperties().get(0).getId());
        assertEquals(entity1.getName(), response.getProperties().get(0).getName());
        assertEquals(entity1.getLocation(), response.getProperties().get(0).getLocation());
        assertEquals(entity1.isAvailability(), response.getProperties().get(0).isAvailability());
        assertEquals(entity1.getImageUrl(), response.getProperties().get(0).getImageUrl());
        assertEquals(entity1.getPrice(), response.getProperties().get(0).getPrice(), 0.001);
        assertEquals(entity2.getId(), response.getProperties().get(1).getId());
        assertEquals(entity2.getName(), response.getProperties().get(1).getName());
        assertEquals(entity2.getLocation(), response.getProperties().get(1).getLocation());
        assertEquals(entity2.isAvailability(), response.getProperties().get(1).isAvailability());
        assertEquals(entity2.getImageUrl(), response.getProperties().get(1).getImageUrl());
        assertEquals(entity2.getPrice(), response.getProperties().get(1).getPrice(), 0.001);
    }

    @Test
     void test_successfully_update_property_with_valid_data() {
        // Given
        PropertyModel updatedProperty = new PropertyModel();
        updatedProperty.setId(1L);
        updatedProperty.setName("Updated Property");
        updatedProperty.setLocation("Medellin");
        updatedProperty.setAvailability(true);
        updatedProperty.setImageUrl("https://example.com/updated_image.jpg");
        updatedProperty.setPrice(3000000);

        PropertyEntity existingEntity = new PropertyEntity();
        existingEntity.setId(1L);
        existingEntity.setName("Existing Property");
        existingEntity.setLocation("Medellin");
        existingEntity.setAvailability(true);
        existingEntity.setImageUrl("https://example.com/image.jpg");
        existingEntity.setPrice(2000000);
        existingEntity.setCreatedAt(LocalDateTime.now());

        Mockito.when(repositoryAdapter.findById(updatedProperty.getId())).thenReturn(Optional.of(existingEntity));
        Mockito.when(repositoryAdapter.save(Mockito.any(PropertyEntity.class))).thenAnswer(invocation -> {
            PropertyEntity savedEntity = invocation.getArgument(0);
            return savedEntity;
        });

        // When
        PropertyModel result = jpaPropertyRepositoryAdapter.updateProperty(updatedProperty);

        // Then
        assertEquals(updatedProperty.getName(), result.getName());
        assertEquals(updatedProperty.getLocation(), result.getLocation());
        assertEquals(updatedProperty.isAvailability(), result.isAvailability());
        assertEquals(updatedProperty.getImageUrl(), result.getImageUrl());
        assertEquals(updatedProperty.getPrice(), result.getPrice(), 0.001);
    }


    @Test
     void test_update_property_property_does_not_exist() {
        // Given
        PropertyModel updatedProperty = new PropertyModel(1L, "Updated Property", "Medellin", true, "https://example.com/image.jpg", 3000000.0);
        when(repositoryAdapter.findById(updatedProperty.getId())).thenReturn(Optional.empty());

        // When/Then
        assertThrows(IllegalArgumentException.class, () -> jpaPropertyRepositoryAdapter.updateProperty(updatedProperty));
    }

    @Test
     void test_not_modify_location_or_price_of_leased_property() {
        // Given
        PropertyModel updatedProperty = new PropertyModel(1L, "Updated Property", "Medellin", false, "https://example.com/image.jpg", 3000000.0);
        PropertyEntity existingEntity = new PropertyEntity(1L, "Existing Property", "Medellin", false, "https://example.com/image.jpg", 2000000.0, LocalDateTime.now());
        when(repositoryAdapter.findById(updatedProperty.getId())).thenReturn(Optional.of(existingEntity));

        // When/Then
        assertThrows(IllegalArgumentException.class, () -> jpaPropertyRepositoryAdapter.updateProperty(updatedProperty));
    }

    @Test
     void test_not_modify_location_of_leased_property() {
        // Given
        PropertyModel updatedProperty = new PropertyModel(1L, "Updated Property", "Bogota", false, "https://example.com/newimage.jpg", 2500000.0);
        PropertyEntity existingEntity = new PropertyEntity(1L, "Existing Property", "Medellin", false, "https://example.com/image.jpg", 2500000.0, LocalDateTime.now());
        when(repositoryAdapter.findById(updatedProperty.getId())).thenReturn(Optional.of(existingEntity));

        // When/Then
        assertThrows(IllegalArgumentException.class, () -> jpaPropertyRepositoryAdapter.updateProperty(updatedProperty));
    }

    @Test
     void test_deleteProperty_lessThanMonthOld() {
        // Given
        Long id = 1L;
        LocalDateTime createdAt = LocalDateTime.now().minusDays(29);
        PropertyEntity entity = new PropertyEntity(id, "Property 1", "Medellin", true, "image.jpg", 1000000, createdAt);
        when(repositoryAdapter.findById(id)).thenReturn(Optional.of(entity));

        // When
        jpaPropertyRepositoryAdapter.deleteProperty(id);

        // Then
        verify(repositoryAdapter).save(entity);
        assertFalse(entity.isAvailability());
    }

    @Test
     void test_deleteProperty_moreThanMonthOld() {
        // Given
        Long id = 1L;
        LocalDateTime createdAt = LocalDateTime.now().minusDays(31);
        PropertyEntity entity = new PropertyEntity(id, "Property 1", "Medellin", true, "image.jpg", 1000000, createdAt);
        when(repositoryAdapter.findById(id)).thenReturn(Optional.of(entity));

        // When/Then
        assertThrows(IllegalArgumentException.class, () -> {
            jpaPropertyRepositoryAdapter.deleteProperty(id);
        }, "Properties that are more than a month old cannot be deleted");
    }

    @Test
     void test_property_available_for_rent_and_successfully_rented() {
        // Given
        Long propertyId = 1L;
        PropertyEntity propertyEntity = new PropertyEntity();
        propertyEntity.setId(propertyId);
        propertyEntity.setAvailability(true);

        given(repositoryAdapter.findById(propertyId)).willReturn(Optional.of(propertyEntity));

        // When
        jpaPropertyRepositoryAdapter.rentProperty(propertyId);

        // Then
        assertFalse(propertyEntity.isAvailability());
        verify(repositoryAdapter).save(propertyEntity);
    }

    @Test
     void test_property_already_rented() {
        // Given
        Long propertyId = 1L;
        PropertyEntity propertyEntity = new PropertyEntity();
        propertyEntity.setId(propertyId);
        propertyEntity.setAvailability(false);

        given(repositoryAdapter.findById(propertyId)).willReturn(Optional.of(propertyEntity));

        // When/Then
        assertThrows(IllegalArgumentException.class, () -> jpaPropertyRepositoryAdapter.rentProperty(propertyId));
        verify(repositoryAdapter, never()).save(any(PropertyEntity.class));
    }

    @Test
     void test_shouldReturnAllPropertiesWhenRepositoryNotEmpty() {
        // Given
        List<PropertyEntity> entities = new ArrayList<>();
        PropertyEntity property1 = new PropertyEntity();
        property1.setId(1L);
        property1.setName("Property 1");
        property1.setLocation("Medellin");
        property1.setAvailability(true);
        property1.setImageUrl("image1.jpg");
        property1.setPrice(1500000);
        entities.add(property1);

        PropertyEntity property2 = new PropertyEntity();
        property2.setId(2L);
        property2.setName("Property 2");
        property2.setLocation("Bogota");
        property2.setAvailability(true);
        property2.setImageUrl("image2.jpg");
        property2.setPrice(2500000);
        entities.add(property2);

        PropertyEntity property3 = new PropertyEntity();
        property3.setId(3L);
        property3.setName("Property 3");
        property3.setLocation("Cali");
        property3.setAvailability(true);
        property3.setImageUrl("image3.jpg");
        property3.setPrice(1800000);
        entities.add(property3);

        Mockito.when(repositoryAdapter.findAll()).thenReturn(entities);

        // When
        List<PropertyModel> result = jpaPropertyRepositoryAdapter.findAllProperties();

        // Then
        assertEquals(3, result.size());
        assertEquals("Property 1", result.get(0).getName());
        assertEquals("Medellin", result.get(0).getLocation());
        assertEquals(true, result.get(0).isAvailability());
        assertEquals("image1.jpg", result.get(0).getImageUrl());
        assertEquals(1500000, result.get(0).getPrice(), 0.001);
        assertEquals("Property 2", result.get(1).getName());
        assertEquals("Bogota", result.get(1).getLocation());
        assertEquals(true, result.get(1).isAvailability());
        assertEquals("image2.jpg", result.get(1).getImageUrl());
        assertEquals(2500000, result.get(1).getPrice(), 0.001);
        assertEquals("Property 3", result.get(2).getName());
        assertEquals("Cali", result.get(2).getLocation());
        assertEquals(true, result.get(2).isAvailability());
        assertEquals("image3.jpg", result.get(2).getImageUrl());
        assertEquals(1800000, result.get(2).getPrice(), 0.001);
    }

    @Test
     void test_property_with_same_name_already_exists() {
        // Given
        PropertyModel inputModel = new PropertyModel();
        inputModel.setName("Test House");
        inputModel.setLocation("Medellin");
        inputModel.setAvailability(true);
        inputModel.setImageUrl("http://example.com/image.jpg");
        inputModel.setPrice(3000000);

        PropertyEntity mockedExistingEntity = new PropertyEntity();
        mockedExistingEntity.setId(1L);
        mockedExistingEntity.setName("Test House");

        when(repositoryAdapter.findByName(anyString())).thenReturn(any());

        // Then
        assertThrows(IllegalArgumentException.class, () -> jpaPropertyRepositoryAdapter.createProperty(inputModel));
    }

    @Test
     void test_property_location_not_valid() {
        // Given
        PropertyModel inputModel = new PropertyModel();
        inputModel.setName("Test House");
        inputModel.setLocation("Invalid Location");
        inputModel.setAvailability(true);
        inputModel.setImageUrl("http://example.com/image.jpg");
        inputModel.setPrice(3000000);

        // Then
        assertThrows(IllegalArgumentException.class, () -> jpaPropertyRepositoryAdapter.createProperty(inputModel));
    }

    @Test
     void test_properties_in_bogota_and_cali_must_have_price_greater_than_2000000() {
        // Given
        PropertyModel inputModel = new PropertyModel();
        inputModel.setName("Test House");
        inputModel.setLocation("Bogota");
        inputModel.setAvailability(true);
        inputModel.setImageUrl("http://example.com/image.jpg");
        inputModel.setPrice(1000000);

        // Then
        assertThrows(IllegalArgumentException.class, () -> jpaPropertyRepositoryAdapter.createProperty(inputModel));
    }

}

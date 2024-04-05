package com.example.pruebatecnicabackend.infrastructure.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class PropertyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    @NotBlank(message = "El nombre no puede estar vacio")
    private String name;
    @NotBlank(message = "La ubicacion no puede estar vacia")
    private String location;
    private boolean availability = true;
    @NotBlank(message = "La URL de la imagen no puede estar vacia")
    private String imageUrl;
    @NotNull(message = "El precio no puede ser nulo")
    @Positive(message = "El precio debe ser mayor a 0")
    private double price;

}

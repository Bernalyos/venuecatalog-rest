package com.codeup.venuecatalog_rest.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


/**
 * Representa un recinto (venue) donde se realizan los eventos.
 */
@Entity
@Table(name = "venues")

public class VenueEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre del recinto. No puede estar vacío ni exceder 150 caracteres.
    @NotBlank(message = "El nombre del recinto es obligatorio.")
    @Size(max = 150, message = "El nombre no debe superar los 150 caracteres.")
    @Column(nullable = false, unique = true)
    private String name;

    // Ciudad donde se encuentra el recinto.
    @NotBlank(message = "La ciudad es obligatoria.")
    @Size(max = 100, message = "El nombre de la ciudad no debe superar los 100 caracteres.")
    @Column(nullable = false)
    private String city;

    // --- Getters y Setters ---

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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}

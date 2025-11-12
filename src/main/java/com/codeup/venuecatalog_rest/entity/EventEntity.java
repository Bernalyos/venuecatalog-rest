package com.codeup.venuecatalog_rest.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "events", uniqueConstraints = @UniqueConstraint( columnNames = "name"))
public class EventEntity {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Size
    @Column
    private String name;

    @Size
    private String description;

    @Future
    @NotNull
    @Column
    private LocalDateTime dateStart;

    @ManyToOne
    @JoinColumn(name = "venue_id")
    private VenueEntity venue;

    @NotBlank
    @Column
    private String category;

    public EventEntity(Long id, String name, String description, LocalDateTime dateStart, VenueEntity venue, String category){
        this.id = id;
        this.name = name;
        this.description = description;
        this.dateStart = dateStart;
        this.venue = venue;
        this.category = category;
    }

    // gett y sett

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateStart() {
        return dateStart;
    }

    public void setDateStart(LocalDateTime dateStart) {
        this.dateStart = dateStart;
    }

    public VenueEntity getVenue() {
        return venue;
    }

    public void setVenue(VenueEntity venue) {
        this.venue = venue;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


}

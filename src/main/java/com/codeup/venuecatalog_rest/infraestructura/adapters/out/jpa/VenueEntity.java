package com.codeup.venuecatalog_rest.infraestructura.adapters.out.jpa;

import jakarta.persistence.*;

@Entity
@Table(name = "Venues")
public class VenueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;

    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private java.util.List<EventEntity> events = new java.util.ArrayList<>();

    // Getters & Setters

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

    public java.util.List<EventEntity> getEvents() {
        return events;
    }

    public void setEvents(java.util.List<EventEntity> events) {
        this.events = events;
    }

    public void addEvent(EventEntity event) {
        events.add(event);
        event.setVenue(this);
    }

    public void removeEvent(EventEntity event) {
        events.remove(event);
        event.setVenue(null);
    }
}

package com.codeup.venuecatalog_rest.domain.model;

import java.time.LocalDate;

public class Event {

    private Long id;
    private String name;
    private String description;
    private LocalDate date;
    private Long venueId;

    public Event() {
    }

    public Event(Long id, String name, String description, LocalDate date, Long venueId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
        this.venueId = venueId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getVenueId() {
        return venueId;
    }

    public void setVenueId(Long venueId) {
        this.venueId = venueId;
    }

}

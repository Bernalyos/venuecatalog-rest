package com.codeup.venuecatalog_rest.domain.model;

import java.time.LocalDate;

public class Event {

    private Long id;
    private String name;
    private LocalDate date;
    private String city;
    private Long venueId;

    public Event() {}

    public Event(Long id, String name, LocalDate date, String city, Long venueId) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.city = city;
        this.venueId = venueId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public Long getVenueId() { return venueId; }
    public void setVenueId(Long venueId) { this.venueId = venueId; }

}

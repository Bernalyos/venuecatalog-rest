package com.codeup.venuecatalog_rest.dto;

import java.time.LocalDateTime;

/**
 * DTO for EventEntity used in API requests/responses.
 */
public class EventDto {

    private Long id;
    private String name;
    private String description;
    private LocalDateTime dateStart;
    private Long venueId;
    private String category;

    public EventDto() {
    }

    public EventDto(Long id, String name, String description, LocalDateTime dateStart, Long venueId, String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.dateStart = dateStart;
        this.venueId = venueId;
        this.category = category;
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

    public LocalDateTime getDateStart() {
        return dateStart;
    }

    public void setDateStart(LocalDateTime dateStart) {
        this.dateStart = dateStart;
    }

    public Long getVenueId() {
        return venueId;
    }

    public void setVenueId(Long venueId) {
        this.venueId = venueId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

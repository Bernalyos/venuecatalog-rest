package com.codeup.venuecatalog_rest.mapper;

import com.codeup.venuecatalog_rest.dto.EventDto;
import com.codeup.venuecatalog_rest.entity.EventEntity;
import com.codeup.venuecatalog_rest.entity.VenueEntity;

/**
 * Simple mapper between EventEntity and EventDto.
 * These are small helpers — consider using MapStruct for larger projects.
 */
public final class EventMapper {

    private EventMapper() {}

    public static EventDto toDto(EventEntity entity) {
        if (entity == null) return null;
        Long venueId = (entity.getVenue() != null) ? entity.getVenue().getId() : null;
        return new EventDto(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getDateStart(),
                venueId,
                entity.getCategory()
        );
    }

    /**
     * Convert DTO to entity. If venue argument is provided it will be set in the entity.
     */
    public static EventEntity toEntity(EventDto dto, VenueEntity venue) {
        if (dto == null) return null;
        EventEntity e = new EventEntity(
                dto.getId(),
                dto.getName(),
                dto.getDescription(),
                dto.getDateStart(),
                venue,
                dto.getCategory()
        );
        return e;
    }
}

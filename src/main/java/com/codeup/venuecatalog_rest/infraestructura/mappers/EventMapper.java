package com.codeup.venuecatalog_rest.infraestructura.mappers;

import com.codeup.venuecatalog_rest.domain.model.Event;
import com.codeup.venuecatalog_rest.infraestructura.adapters.out.jpa.EventEntity;
import com.codeup.venuecatalog_rest.infraestructura.dto.EventDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventMapper {

    // Domain <-> DTO (For RestAdapter)
    EventDTO toDto(Event event);

    Event toDomain(EventDTO dto);

    // Domain <-> Entity (For JpaAdapter)
    @Mapping(target = "venue", ignore = true)
    EventEntity toEntity(Event domain);

    @Mapping(target = "venueId", source = "venue.id")
    Event toDomain(EventEntity entity);
}

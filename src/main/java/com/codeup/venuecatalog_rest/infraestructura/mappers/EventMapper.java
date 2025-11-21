package com.codeup.venuecatalog_rest.infraestructura.mappers;

import com.codeup.venuecatalog_rest.domain.model.Event;
import com.codeup.venuecatalog_rest.infraestructura.adapters.out.jpa.EventEntity;
import com.codeup.venuecatalog_rest.infraestructura.dto.EventDTO;
import org.mapstruct.Mapper;


@Mapper (componentModel = "spring")
public interface EventMapper {

    EventDTO toDto(Event event);

    Event toDomain(EventDTO dto);

    EventEntity toEntity(Event domain);

    Event toDomain(EventEntity entity);
}

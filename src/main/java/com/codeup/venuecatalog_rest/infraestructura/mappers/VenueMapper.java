package com.codeup.venuecatalog_rest.infraestructura.mappers;

import com.codeup.venuecatalog_rest.domain.model.Venue;
import com.codeup.venuecatalog_rest.infraestructura.adapters.out.jpa.VenueEntity;
import com.codeup.venuecatalog_rest.infraestructura.dto.VenueDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VenueMapper {

    VenueDTO toDto(Venue venue);

    Venue toDomain(VenueDTO dto);

    VenueEntity toEntity(Venue domain);

    Venue toDomain(VenueEntity entity);
}
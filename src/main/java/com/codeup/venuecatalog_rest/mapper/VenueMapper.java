package com.codeup.venuecatalog_rest.mapper;

import com.codeup.venuecatalog_rest.dto.VenueDto;
import com.codeup.venuecatalog_rest.entity.VenueEntity;

public final class VenueMapper {

    private VenueMapper() {}

    public static VenueDto toDto(VenueEntity entity) {
        if (entity == null) return null;
        return new VenueDto(entity.getId(), entity.getName(), entity.getCity());
    }

    public static VenueEntity toEntity(VenueDto dto) {
        if (dto == null) return null;
        VenueEntity v = new VenueEntity();
        v.setId(dto.getId());
        v.setName(dto.getName());
        v.setCity(dto.getCity());
        return v;
    }
}

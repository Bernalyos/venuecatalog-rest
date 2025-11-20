package com.codeup.venuecatalog_rest.domain.ports.in;

import com.codeup.venuecatalog_rest.domain.model.Venue;

import java.util.Optional;

public interface GetVenueUseCase {

    Optional<Venue> getById(Long id);

}

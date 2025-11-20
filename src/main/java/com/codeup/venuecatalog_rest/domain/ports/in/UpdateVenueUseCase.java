package com.codeup.venuecatalog_rest.domain.ports.in;

import com.codeup.venuecatalog_rest.domain.model.Venue;

public interface UpdateVenueUseCase {

    Venue update(Long id, Venue venue);
}

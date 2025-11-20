package com.codeup.venuecatalog_rest.domain.ports.in;

import com.codeup.venuecatalog_rest.domain.model.Venue;

public interface CreateVenueUseCase {

    Venue create(Venue venue);
}

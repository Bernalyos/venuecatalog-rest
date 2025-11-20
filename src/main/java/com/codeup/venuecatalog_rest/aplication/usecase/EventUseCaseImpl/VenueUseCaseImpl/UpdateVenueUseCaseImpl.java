package com.codeup.venuecatalog_rest.aplication.usecase.EventUseCaseImpl.VenueUseCaseImpl;

import com.codeup.venuecatalog_rest.domain.model.Venue;
import com.codeup.venuecatalog_rest.domain.ports.in.UpdateVenueUseCase;
import com.codeup.venuecatalog_rest.domain.ports.out.VenueRepositoryPort;

public class UpdateVenueUseCaseImpl implements UpdateVenueUseCase {

    private final VenueRepositoryPort venueRepositoryPort;

    public UpdateVenueUseCaseImpl(VenueRepositoryPort venueRepositoryPort) {
        this.venueRepositoryPort = venueRepositoryPort;
    }

    @Override
    public Venue update(Long id, Venue venue) {
        venue.setId(id);
        return venueRepositoryPort.save(venue);
    }
}

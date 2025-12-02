package com.codeup.venuecatalog_rest.aplication.usecase.EventUseCaseImpl.VenueUseCaseImpl;

import com.codeup.venuecatalog_rest.domain.model.Venue;
import com.codeup.venuecatalog_rest.domain.ports.in.CreateVenueUseCase;
import com.codeup.venuecatalog_rest.domain.ports.out.VenueRepositoryPort;

public class CreateVenueUseCaseImpl implements CreateVenueUseCase {

    private final VenueRepositoryPort venueRepositoryPort;

    public CreateVenueUseCaseImpl(VenueRepositoryPort venueRepositoryPort) {
        this.venueRepositoryPort = venueRepositoryPort;
    }

    @Override
    public Venue create(Venue venue) {
        return venueRepositoryPort.save(venue);
    }
}

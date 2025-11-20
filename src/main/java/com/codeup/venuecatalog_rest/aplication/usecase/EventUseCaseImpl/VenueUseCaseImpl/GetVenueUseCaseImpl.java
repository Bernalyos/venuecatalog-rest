package com.codeup.venuecatalog_rest.aplication.usecase.EventUseCaseImpl.VenueUseCaseImpl;

import com.codeup.venuecatalog_rest.domain.model.Venue;
import com.codeup.venuecatalog_rest.domain.ports.in.GetVenueUseCase;
import com.codeup.venuecatalog_rest.domain.ports.out.VenueRepositoryPort;

import java.util.Optional;

public class GetVenueUseCaseImpl implements GetVenueUseCase {

    private final VenueRepositoryPort venueRepositoryPort;

    public GetVenueUseCaseImpl(VenueRepositoryPort venueRepositoryPort) {
        this.venueRepositoryPort = venueRepositoryPort;
    }

    @Override
    public Optional<Venue> getById(Long id) {
        return venueRepositoryPort.findById(id);
    }
}

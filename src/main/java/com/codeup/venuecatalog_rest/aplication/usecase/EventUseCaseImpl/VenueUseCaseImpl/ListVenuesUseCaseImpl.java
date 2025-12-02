package com.codeup.venuecatalog_rest.aplication.usecase.EventUseCaseImpl.VenueUseCaseImpl;

import com.codeup.venuecatalog_rest.domain.model.Venue;
import com.codeup.venuecatalog_rest.domain.ports.in.ListVenuesUseCase;
import com.codeup.venuecatalog_rest.domain.ports.out.VenueRepositoryPort;

import java.util.List;

public class ListVenuesUseCaseImpl implements ListVenuesUseCase {

    private final VenueRepositoryPort venueRepositoryPort;

    public ListVenuesUseCaseImpl(VenueRepositoryPort venueRepositoryPort) {
        this.venueRepositoryPort = venueRepositoryPort;
    }

    @Override
    public List<Venue> list() {
        return venueRepositoryPort.findAll();
    }
}

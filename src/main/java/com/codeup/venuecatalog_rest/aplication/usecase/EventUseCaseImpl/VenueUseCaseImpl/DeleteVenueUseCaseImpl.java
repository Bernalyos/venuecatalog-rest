package com.codeup.venuecatalog_rest.aplication.usecase.EventUseCaseImpl.VenueUseCaseImpl;

import com.codeup.venuecatalog_rest.domain.ports.in.DeleteVenueUseCase;
import com.codeup.venuecatalog_rest.domain.ports.out.VenueRepositoryPort;

public class DeleteVenueUseCaseImpl implements DeleteVenueUseCase {

    private final VenueRepositoryPort venueRepositoryPort;

    public DeleteVenueUseCaseImpl(VenueRepositoryPort venueRepositoryPort) {
        this.venueRepositoryPort = venueRepositoryPort;
    }

    @Override
    public void delete(Long id) {
        venueRepositoryPort.deleteById(id);
    }
}

package com.codeup.venuecatalog_rest.domain.ports.out;

import com.codeup.venuecatalog_rest.domain.model.Venue;

import java.util.List;
import java.util.Optional;

public interface VenueRepositoryPort {

    Venue save(Venue venue); // create + update

    Optional<Venue> findById(Long id);

    List<Venue> findAll();

    void deleteById(Long id);
}

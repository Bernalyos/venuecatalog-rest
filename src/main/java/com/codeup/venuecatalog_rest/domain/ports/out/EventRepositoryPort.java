package com.codeup.venuecatalog_rest.domain.ports.out;

import com.codeup.venuecatalog_rest.domain.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventRepositoryPort {
    Event save(Event event); // create + update

    Optional<Event> findById(Long id);

    List<Event> findAll();

    List<Event> search(String name, java.time.LocalDate date, Long venueId);

    boolean existsByName(String name);

    void deleteById(Long id);
}

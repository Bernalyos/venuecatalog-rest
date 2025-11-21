package com.codeup.venuecatalog_rest.infraestructura.adapters.out.jpa;

import com.codeup.venuecatalog_rest.domain.model.Event;
import com.codeup.venuecatalog_rest.domain.ports.out.EventRepositoryPort;
import com.codeup.venuecatalog_rest.infraestructura.mappers.EventMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class EventJpaAdapter implements EventRepositoryPort {

    private final EventJpaRepository repository;
    private final VenueJpaRepository venueRepository;
    private final EventMapper mapper;

    public EventJpaAdapter(EventJpaRepository repository, VenueJpaRepository venueRepository, EventMapper mapper) {
        this.repository = repository;
        this.venueRepository = venueRepository;
        this.mapper = mapper;
    }

    @Override
    public Event save(Event event) {
        EventEntity entity = mapper.toEntity(event);

        if (event.getVenueId() != null) {
            VenueEntity venue = venueRepository.findById(event.getVenueId())
                    .orElseThrow(() -> new IllegalArgumentException("Venue not found with id: " + event.getVenueId()));
            entity.setVenue(venue);
        }

        EventEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Event> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Event> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Event> search(String name, java.time.LocalDate date, Long venueId) {
        org.springframework.data.jpa.domain.Specification<EventEntity> spec = (root, query, cb) -> {
            java.util.List<jakarta.persistence.criteria.Predicate> predicates = new java.util.ArrayList<>();

            if (name != null && !name.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (date != null) {
                predicates.add(cb.equal(root.get("date"), date));
            }
            if (venueId != null) {
                predicates.add(cb.equal(root.get("venue").get("id"), venueId));
            }

            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };

        return repository.findAll(spec).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByName(String name) {
        return repository.existsByName(name);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}

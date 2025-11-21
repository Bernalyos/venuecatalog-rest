package com.codeup.venuecatalog_rest.infraestructura.adapters.out.jpa;

import com.codeup.venuecatalog_rest.domain.model.Venue;
import com.codeup.venuecatalog_rest.domain.ports.out.VenueRepositoryPort;
import com.codeup.venuecatalog_rest.infraestructura.mappers.VenueMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class VenueJpaAdapter implements VenueRepositoryPort {

    private final VenueJpaRepository repository;
    private final VenueMapper mapper;

    public VenueJpaAdapter(VenueJpaRepository repository, VenueMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Venue save(Venue venue) {
        VenueEntity entity = mapper.toEntity(venue);
        VenueEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Venue> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Venue> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }


    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}

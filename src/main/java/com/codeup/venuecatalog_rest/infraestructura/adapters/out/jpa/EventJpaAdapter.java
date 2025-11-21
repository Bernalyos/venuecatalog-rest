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
    private final EventMapper mapper;

    public EventJpaAdapter(EventJpaRepository repository, EventMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Event save(Event event) {
        EventEntity entity = mapper.toEntity(event);
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
    public boolean existsByName(String name) {
        return repository.existsByName(name);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}

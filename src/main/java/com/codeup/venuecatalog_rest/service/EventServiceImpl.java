package com.codeup.venuecatalog_rest.service;

import com.codeup.venuecatalog_rest.entity.EventEntity;
import com.codeup.venuecatalog_rest.entity.VenueEntity;
import com.codeup.venuecatalog_rest.repository.EventRepository;
import com.codeup.venuecatalog_rest.repository.VenueRepository;
import com.codeup.venuecatalog_rest.execption.ResourceConflictException;
import com.codeup.venuecatalog_rest.execption.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Override
    public EventEntity create(EventEntity event) {
        if (eventRepository.existsByName(event.getName())) {
            throw new ResourceConflictException("An event with this name already exists");
        }
        Long venueId = event.getVenue().getId();
        VenueEntity venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found with id: " + venueId));
        event.setVenue(venue);
        return eventRepository.save(event);
    }

    @Override
    public EventEntity update(Long id, EventEntity updated) {
        EventEntity existing = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setDateStart(updated.getDateStart());
        existing.setCategory(updated.getCategory());
        existing.setVenue(updated.getVenue());
        return eventRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new ResourceNotFoundException("Event not found with id: " + id);
        }
        eventRepository.deleteById(id);
    }

    @Override
    public Optional<EventEntity> findById(Long id) {
        return eventRepository.findById(id);
    }

    @Override
    public Page<EventEntity> findAll(Pageable pageable, String city, String category, LocalDate dateStart) {
        Specification<EventEntity> spec = null;

        if (city != null && !city.isEmpty()) {
            spec = (spec == null) ?
                    (root, query, cb) -> cb.equal(root.join("venue").get("city"), city)
                    : spec.and((root, query, cb) -> cb.equal(root.join("venue").get("city"), city));
        }

        if (category != null && !category.isEmpty()) {
            spec = (spec == null) ?
                    (root, query, cb) -> cb.equal(root.get("category"), category)
                    : spec.and((root, query, cb) -> cb.equal(root.get("category"), category));
        }

        if (dateStart != null) {
            LocalDateTime dateTime = dateStart.atStartOfDay();
            spec = (spec == null) ?
                    (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("dateStart"), dateTime)
                    : spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("dateStart"), dateTime));
        }

        return eventRepository.findAll(spec, pageable);
    }

}


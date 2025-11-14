package com.codeup.venuecatalog_rest.controller;

import com.codeup.venuecatalog_rest.dto.EventDto;
import com.codeup.venuecatalog_rest.entity.EventEntity;
import com.codeup.venuecatalog_rest.entity.VenueEntity;
import com.codeup.venuecatalog_rest.execption.ResourceNotFoundException;
import com.codeup.venuecatalog_rest.mapper.EventMapper;
import com.codeup.venuecatalog_rest.repository.VenueRepository;
import com.codeup.venuecatalog_rest.service.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private VenueRepository venueRepository;

    @GetMapping
    public ResponseEntity<Page<EventDto>> list(
            Pageable pageable,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateStart
    ) {
        Page<EventEntity> page = eventService.findAll(pageable, city, category, dateStart);
        List<EventDto> dtos = page.getContent().stream().map(EventMapper::toDto).collect(Collectors.toList());
        Page<EventDto> dtoPage = new PageImpl<>(dtos, pageable, page.getTotalElements());
        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDto> getById(@PathVariable Long id) {
        Optional<EventEntity> maybe = eventService.findById(id);
        EventEntity entity = maybe.orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
        return ResponseEntity.ok(EventMapper.toDto(entity));
    }

    @PostMapping
    public ResponseEntity<EventDto> create(@Valid @RequestBody EventDto eventDto) {
        VenueEntity venue = null;
        if (eventDto.getVenueId() != null) {
            venue = venueRepository.findById(eventDto.getVenueId())
                    .orElseThrow(() -> new ResourceNotFoundException("Venue not found with id: " + eventDto.getVenueId()));
        }
        EventEntity toSave = EventMapper.toEntity(eventDto, venue);
        EventEntity created = eventService.create(toSave);
        URI location = URI.create(String.format("/api/events/%d", created.getId()));
        return ResponseEntity.created(location).body(EventMapper.toDto(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventDto> update(@PathVariable Long id, @Valid @RequestBody EventDto eventDto) {
        VenueEntity venue = null;
        if (eventDto.getVenueId() != null) {
            venue = venueRepository.findById(eventDto.getVenueId())
                    .orElseThrow(() -> new ResourceNotFoundException("Venue not found with id: " + eventDto.getVenueId()));
        }
        EventEntity updatedEntity = EventMapper.toEntity(eventDto, venue);
        EventEntity updated = eventService.update(id, updatedEntity);
        return ResponseEntity.ok(EventMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

package com.codeup.venuecatalog_rest.infraestructura.adapters.in.web;

import com.codeup.venuecatalog_rest.domain.model.Event;
import com.codeup.venuecatalog_rest.domain.ports.in.*;
import com.codeup.venuecatalog_rest.infraestructura.dto.EventDTO;
import com.codeup.venuecatalog_rest.infraestructura.mappers.EventMapper;
import com.codeup.venuecatalog_rest.infraestructura.validation.ValidationGroups;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * REST adapter for Event operations
 * Implements validation with groups for Create and Update scenarios
 */
@RestController
@RequestMapping("/events")
@Validated
public class EventRestAdapter {
    private final CreateEventUseCase createEventUseCase;
    private final GetEventUseCase getEventUseCase;
    private final ListEventsUseCase listEventsUseCase;
    private final UpdateEventUseCase updateEventUseCase;
    private final DeleteEventUseCase deleteEventUseCase;
    private final EventMapper mapper;

    public EventRestAdapter(
            CreateEventUseCase createEventUseCase,
            GetEventUseCase getEventUseCase,
            ListEventsUseCase listEventsUseCase,
            UpdateEventUseCase updateEventUseCase,
            DeleteEventUseCase deleteEventUseCase,
            EventMapper mapper) {
        this.createEventUseCase = createEventUseCase;
        this.getEventUseCase = getEventUseCase;
        this.listEventsUseCase = listEventsUseCase;
        this.updateEventUseCase = updateEventUseCase;
        this.deleteEventUseCase = deleteEventUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<EventDTO> create(
            @Validated(ValidationGroups.Create.class) @RequestBody EventDTO dto) {
        Event created = createEventUseCase.create(mapper.toDomain(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(created));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<EventDTO> get(@PathVariable Long id) {
        return ResponseEntity.of(
                getEventUseCase.getById(id).map(mapper::toDto));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(
                listEventsUseCase.list().stream()
                        .map(mapper::toDto)
                        .toList());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<EventDTO> update(
            @PathVariable Long id,
            @Validated(ValidationGroups.Update.class) @RequestBody EventDTO dto) {
        Event updated = updateEventUseCase.update(id, mapper.toDomain(dto));
        return ResponseEntity.ok(mapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteEventUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}

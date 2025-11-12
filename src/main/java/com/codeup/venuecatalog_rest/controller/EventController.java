package com.codeup.venuecatalog_rest.controller;

import com.codeup.venuecatalog_rest.entity.EventEntity;
import com.codeup.venuecatalog_rest.execption.ResourceConflictException;
import com.codeup.venuecatalog_rest.execption.ResourceNotFoundException;
import com.codeup.venuecatalog_rest.service.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping
    public ResponseEntity<Page<EventEntity>> list(
            Pageable pageable,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateStart
    ) {
        Page<EventEntity> page = eventService.findAll(pageable, city, category, dateStart);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventEntity> getById(@PathVariable Long id) {
        Optional<EventEntity> maybe = eventService.findById(id);
        return maybe.map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
    }

    @PostMapping
    public ResponseEntity<EventEntity> create(@Valid @RequestBody EventEntity event) {
        EventEntity created = eventService.create(event);
        URI location = URI.create(String.format("/api/events/%d", created.getId()));
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventEntity> update(@PathVariable Long id, @Valid @RequestBody EventEntity event) {
        EventEntity updated = eventService.update(id, event);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Manejo local de excepciones para respuestas HTTP más claras
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<String> handleConflict(ResourceConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

}

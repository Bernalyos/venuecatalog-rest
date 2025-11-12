package com.codeup.venuecatalog_rest.controller;

import com.codeup.venuecatalog_rest.entity.VenueEntity;
import com.codeup.venuecatalog_rest.execption.ResourceConflictException;
import com.codeup.venuecatalog_rest.execption.ResourceNotFoundException;
import com.codeup.venuecatalog_rest.service.VenueService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/venues")
public class VenueController {

    @Autowired
    private VenueService venueService;

    @GetMapping
    public ResponseEntity<List<VenueEntity>> list() {
        List<VenueEntity> venues = venueService.findAll();
        return ResponseEntity.ok(venues);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VenueEntity> getById(@PathVariable Long id) {
        Optional<VenueEntity> maybe = venueService.findById(id);
        return maybe.map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found with id: " + id));
    }

    @PostMapping
    public ResponseEntity<VenueEntity> create(@Valid @RequestBody VenueEntity venue) {
        VenueEntity created = venueService.create(venue);
        URI location = URI.create(String.format("/api/venues/%d", created.getId()));
        return ResponseEntity.created(location).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        venueService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<String> handleConflict(ResourceConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

}

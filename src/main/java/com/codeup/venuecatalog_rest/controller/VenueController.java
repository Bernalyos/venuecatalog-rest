package com.codeup.venuecatalog_rest.controller;

import com.codeup.venuecatalog_rest.dto.VenueDto;
import com.codeup.venuecatalog_rest.entity.VenueEntity;
import com.codeup.venuecatalog_rest.execption.ResourceNotFoundException;
import com.codeup.venuecatalog_rest.mapper.VenueMapper;
import com.codeup.venuecatalog_rest.service.VenueService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/venues")
public class VenueController {

    @Autowired
    private VenueService venueService;

    @GetMapping
    public ResponseEntity<List<VenueDto>> list() {
        List<VenueEntity> venues = venueService.findAll();
        List<VenueDto> dtos = venues.stream().map(VenueMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VenueDto> getById(@PathVariable Long id) {
        Optional<VenueEntity> maybe = venueService.findById(id);
        VenueEntity entity = maybe.orElseThrow(() -> new ResourceNotFoundException("Venue not found with id: " + id));
        return ResponseEntity.ok(VenueMapper.toDto(entity));
    }

    @PostMapping
    public ResponseEntity<VenueDto> create(@Valid @RequestBody VenueDto venueDto) {
        VenueEntity toSave = VenueMapper.toEntity(venueDto);
        VenueEntity created = venueService.create(toSave);
        URI location = URI.create(String.format("/api/venues/%d", created.getId()));
        return ResponseEntity.created(location).body(VenueMapper.toDto(created));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        venueService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

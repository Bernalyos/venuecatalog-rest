package com.codeup.venuecatalog_rest.infraestructura.adapters.in.web;

import com.codeup.venuecatalog_rest.domain.model.Venue;
import com.codeup.venuecatalog_rest.domain.ports.in.*;
import com.codeup.venuecatalog_rest.infraestructura.dto.VenueDTO;
import com.codeup.venuecatalog_rest.infraestructura.mappers.VenueMapper;
import com.codeup.venuecatalog_rest.infraestructura.validation.ValidationGroups;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * REST adapter for Venue operations
 * Implements validation with groups for Create and Update scenarios
 */
@RestController
@RequestMapping("/venues")
@Validated
public class VenueRestAdapter {

    private final CreateVenueUseCase createVenueUseCase;
    private final GetVenueUseCase getVenueUseCase;
    private final ListVenuesUseCase listVenuesUseCase;
    private final UpdateVenueUseCase updateVenueUseCase;
    private final DeleteVenueUseCase deleteVenueUseCase;
    private final VenueMapper mapper;

    public VenueRestAdapter(
            CreateVenueUseCase createVenueUseCase,
            GetVenueUseCase getVenueUseCase,
            ListVenuesUseCase listVenuesUseCase,
            UpdateVenueUseCase updateVenueUseCase,
            DeleteVenueUseCase deleteVenueUseCase,
            VenueMapper mapper) {
        this.createVenueUseCase = createVenueUseCase;
        this.getVenueUseCase = getVenueUseCase;
        this.listVenuesUseCase = listVenuesUseCase;
        this.updateVenueUseCase = updateVenueUseCase;
        this.deleteVenueUseCase = deleteVenueUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<VenueDTO> create(
            @Validated(ValidationGroups.Create.class) @RequestBody VenueDTO dto) {
        Venue created = createVenueUseCase.create(mapper.toDomain(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(created));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<VenueDTO> get(@PathVariable Long id) {
        return ResponseEntity.of(
                getVenueUseCase.getById(id).map(mapper::toDto));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(
                listVenuesUseCase.list().stream()
                        .map(mapper::toDto)
                        .toList());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<VenueDTO> update(
            @PathVariable Long id,
            @Validated(ValidationGroups.Update.class) @RequestBody VenueDTO dto) {
        Venue updated = updateVenueUseCase.update(id, mapper.toDomain(dto));
        return ResponseEntity.ok(mapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteVenueUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}

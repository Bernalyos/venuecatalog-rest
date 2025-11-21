package com.codeup.venuecatalog_rest.infraestructura.adapters.in.web;

import com.codeup.venuecatalog_rest.domain.model.Venue;
import com.codeup.venuecatalog_rest.domain.ports.in.*;
import com.codeup.venuecatalog_rest.infraestructura.dto.VenueDTO;
import com.codeup.venuecatalog_rest.infraestructura.mappers.VenueMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/venues")
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
            VenueMapper mapper
    ) {
        this.createVenueUseCase = createVenueUseCase;
        this.getVenueUseCase = getVenueUseCase;
        this.listVenuesUseCase = listVenuesUseCase;
        this.updateVenueUseCase = updateVenueUseCase;
        this.deleteVenueUseCase = deleteVenueUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<VenueDTO> create(@RequestBody VenueDTO dto) {
        Venue created = createVenueUseCase.create(mapper.toDomain(dto));
        return ResponseEntity.ok(mapper.toDto(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VenueDTO> get(@PathVariable Long id) {
        return ResponseEntity.of(
                getVenueUseCase.getById(id).map(mapper::toDto)
        );
    }

    @GetMapping
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(
                listVenuesUseCase.list().stream()
                        .map(mapper::toDto)
                        .toList()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<VenueDTO> update(@PathVariable Long id, @RequestBody VenueDTO dto) {
        Venue updated = updateVenueUseCase.update(id, mapper.toDomain(dto));
        return ResponseEntity.ok(mapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteVenueUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}

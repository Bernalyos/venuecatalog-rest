package com.codeup.venuecatalog_rest.controller;

import com.codeup.venuecatalog_rest.DTO.VenueDTO;
import com.codeup.venuecatalog_rest.service.VenueService;

import jakarta.validation.Valid;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/venues")
public class VenueController {

    private final VenueService svc;

    public VenueController(VenueService svc){ this.svc = svc;}

    @PostMapping
    public ResponseEntity<VenueDTO> create(@Valid @RequestBody VenueDTO dto){
        VenueDTO created = svc.create(dto);
        return ResponseEntity.created(URI.create("/venues/" + created.getId())).body(created);
    }

    @GetMapping
    public ResponseEntity<List<VenueDTO>> getAll(){
        return ResponseEntity.ok(svc.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VenueDTO> getById(@PathVariable Long id){
        return ResponseEntity.ok(svc.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VenueDTO> update(@PathVariable Long id, @Valid @RequestBody VenueDTO dto){
        return ResponseEntity.ok(svc.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }
}


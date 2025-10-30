package com.codeup.venuecatalog_rest.controller;

import com.codeup.venuecatalog_rest.DTO.EventDTO;
import com.codeup.venuecatalog_rest.service.EventService;

import jakarta.validation.Valid;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService svc;

    public EventController(EventService svc ){ this.svc = svc;}


    @PostMapping
    public ResponseEntity<EventDTO> create(@Valid @RequestBody EventDTO dto){
        EventDTO created = svc.create(dto);
        return ResponseEntity.created(URI.create("/events/" + created.getId())).body(created);
    }

    @GetMapping
    public ResponseEntity<List<EventDTO>> getAll(){
        return ResponseEntity.ok(svc.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getById(@PathVariable Long id){
        return ResponseEntity.ok(svc.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventDTO> update(@PathVariable Long id, @Valid @RequestBody EventDTO dto){
        return ResponseEntity.ok(svc.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }
}
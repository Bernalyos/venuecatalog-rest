package com.codeup.venuecatalog_rest.service;

import com.codeup.venuecatalog_rest.DTO.EventDTO;
import com.codeup.venuecatalog_rest.DTO.VenueDTO;
import com.codeup.venuecatalog_rest.exception.NotFoundException;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class VenueService {

    private final Map<Long, VenueDTO> store = new LinkedHashMap<>();
    private final AtomicLong idGen = new AtomicLong(1);

    public VenueDTO create (VenueDTO v){
        Long id = idGen.getAndIncrement();
        v.setId(id);
        store.put(id, v);
        return v;

    }

    public List<VenueDTO> findAll() {
        return new ArrayList<>(store.values());
    }

    public VenueDTO findById(Long id) {
        VenueDTO v = store.get(id);
        if (v == null) throw new NotFoundException("Venue not found: " + id);
        return v;
    }

    public VenueDTO update(Long id, VenueDTO updated) {
        if (!store.containsKey(id)) throw new NotFoundException("Venue not found: " + id);
        updated.setId(id);
        store.put(id, updated);
        return updated;
    }

    public void delete(Long id) {
        if (!store.containsKey(id)) throw new NotFoundException("Venue not found: " + id);
        store.remove(id);
    }

    public void clear() { store.clear(); idGen.set(1); } // útil para tests/profile test
}



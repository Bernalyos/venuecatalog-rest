package com.codeup.venuecatalog_rest.service;


import com.codeup.venuecatalog_rest.DTO.EventDTO;
import com.codeup.venuecatalog_rest.exception.NotFoundException;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
public class EventService {

    private final Map<Long, EventDTO> store = new LinkedHashMap<>();
    private final AtomicLong idGen = new AtomicLong(1);

    public EventDTO create(@org.jetbrains.annotations.NotNull EventDTO ev) {
        Long id = idGen.getAndIncrement();
        ev.setId(id);
        store.put(id, ev);
        return ev;
    }

    public List<EventDTO> findAll() {
        return new ArrayList<>(store.values());
    }

    public EventDTO findById(Long id) {
        EventDTO ev = store.get(id);
        if (ev == null) throw new NotFoundException("Event not found: " + id);
        return ev;
    }

    public EventDTO update(Long id, EventDTO updated) {
        if (!store.containsKey(id)) throw new NotFoundException("Event not found: " + id);
        updated.setId(id);
        store.put(id, updated);
        return updated;
    }

    public void delete(Long id) {
        if (!store.containsKey(id)) throw new NotFoundException("Event not found: " + id);
        store.remove(id);
    }

    public void clear() { store.clear(); idGen.set(1); } // útil para tests/profile test
}

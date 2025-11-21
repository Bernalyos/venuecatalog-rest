package com.codeup.venuecatalog_rest;

import com.codeup.venuecatalog_rest.domain.model.Event;
import com.codeup.venuecatalog_rest.infraestructura.adapters.out.jpa.EventEntity;
import com.codeup.venuecatalog_rest.infraestructura.adapters.out.jpa.EventJpaAdapter;
import com.codeup.venuecatalog_rest.infraestructura.adapters.out.jpa.VenueEntity;
import com.codeup.venuecatalog_rest.infraestructura.adapters.out.jpa.VenueJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class QueryOptimizationTest {

    @Autowired
    private EventJpaAdapter eventAdapter;

    @Autowired
    private VenueJpaRepository venueRepository;

    @Test
    public void testSearchFilters() {
        // Setup
        VenueEntity venue1 = new VenueEntity();
        venue1.setName("Venue A");
        venue1.setLocation("Loc A");
        venueRepository.save(venue1);

        VenueEntity venue2 = new VenueEntity();
        venue2.setName("Venue B");
        venue2.setLocation("Loc B");
        venueRepository.save(venue2);

        Event event1 = new Event(null, "Concert A", "Desc A", LocalDate.of(2023, 10, 1), venue1.getId());
        eventAdapter.save(event1);

        Event event2 = new Event(null, "Concert B", "Desc B", LocalDate.of(2023, 10, 2), venue2.getId());
        eventAdapter.save(event2);

        Event event3 = new Event(null, "Festival A", "Desc C", LocalDate.of(2023, 10, 1), venue1.getId());
        eventAdapter.save(event3);

        // Test 1: Filter by Name
        List<Event> resultsName = eventAdapter.search("Concert", null, null);
        assertEquals(2, resultsName.size());

        // Test 2: Filter by Date
        List<Event> resultsDate = eventAdapter.search(null, LocalDate.of(2023, 10, 1), null);
        assertEquals(2, resultsDate.size());

        // Test 3: Filter by Venue
        List<Event> resultsVenue = eventAdapter.search(null, null, venue1.getId());
        assertEquals(2, resultsVenue.size());

        // Test 4: Combined Filter
        List<Event> resultsCombined = eventAdapter.search("Concert", LocalDate.of(2023, 10, 1), venue1.getId());
        assertEquals(1, resultsCombined.size());
        assertEquals("Concert A", resultsCombined.get(0).getName());
    }

    @Test
    public void testFindAllEntityGraph() {
        // Setup
        VenueEntity venue = new VenueEntity();
        venue.setName("Graph Venue");
        venue.setLocation("Graph Loc");
        venueRepository.save(venue);

        Event event = new Event(null, "Graph Event", "Desc", LocalDate.now(), venue.getId());
        eventAdapter.save(event);

        // Execute
        List<Event> events = eventAdapter.findAll();

        // Verify
        assertFalse(events.isEmpty());
        // Note: Verifying eager loading programmatically in a simple integration test
        // is tricky without checking persistence unit util or logs.
        // But ensuring it runs without error confirms the query structure is valid.
    }
}

package com.codeup.venuecatalog_rest;

import com.codeup.venuecatalog_rest.infraestructura.adapters.out.jpa.EventEntity;
import com.codeup.venuecatalog_rest.infraestructura.adapters.out.jpa.EventJpaRepository;
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
public class RelationshipIntegrationTest {

    @Autowired
    private VenueJpaRepository venueRepository;

    @Autowired
    private EventJpaRepository eventRepository;

    @Test
    public void testVenueEventRelationship() {
        // 1. Create Venue
        VenueEntity venue = new VenueEntity();
        venue.setName("Test Venue");
        venue.setLocation("Test Location");

        // 2. Create Event and add to Venue
        EventEntity event = new EventEntity();
        event.setName("Test Event");
        event.setDescription("Test Description");
        event.setDate(LocalDate.now());

        venue.addEvent(event); // Helper method sets bidirectional relationship

        // 3. Save Venue (Cascade should save Event)
        VenueEntity savedVenue = venueRepository.save(venue);

        assertNotNull(savedVenue.getId());
        assertEquals(1, savedVenue.getEvents().size());

        // 4. Verify Event is saved and has Venue
        EventEntity savedEvent = savedVenue.getEvents().get(0);
        assertNotNull(savedEvent.getId());
        assertEquals(savedVenue.getId(), savedEvent.getVenue().getId());

        // 5. Verify Event exists in EventRepository
        assertTrue(eventRepository.findById(savedEvent.getId()).isPresent());

        // 6. Delete Venue (Cascade should delete Event)
        venueRepository.delete(savedVenue);

        // 7. Verify Event is deleted
        assertFalse(eventRepository.findById(savedEvent.getId()).isPresent());
    }
}

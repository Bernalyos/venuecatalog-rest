package com.codeup.venuecatalog_rest.service;


import com.codeup.venuecatalog_rest.entity.VenueEntity;
import com.codeup.venuecatalog_rest.execption.ResourceConflictException;
import com.codeup.venuecatalog_rest.execption.ResourceNotFoundException;
import com.codeup.venuecatalog_rest.repository.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class VenueServiceImpl implements VenueService {

    @Autowired
    private VenueRepository venueRepository;

    @Override
    public VenueEntity create(VenueEntity venue) {
        if (venueRepository.existsByName(venue.getName())) {
            throw new ResourceConflictException("A venue with this name already exists");
        }
        return venueRepository.save(venue);
    }

    @Override
    public List<VenueEntity> findAll() {
        return venueRepository.findAll();
    }

    @Override
    public Optional<VenueEntity> findById(Long id) {
        return venueRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        if (!venueRepository.existsById(id)) {
            throw new ResourceNotFoundException("Venue not found with id: " + id);
        }
        venueRepository.deleteById(id);
    }
}

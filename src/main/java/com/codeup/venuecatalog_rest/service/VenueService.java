package com.codeup.venuecatalog_rest.service;

import com.codeup.venuecatalog_rest.entity.VenueEntity;

import java.util.List;
import java.util.Optional;

public interface VenueService {
    VenueEntity create(VenueEntity venue);
    List<VenueEntity> findAll();
    Optional<VenueEntity> findById(Long id);
    void delete(Long id);
}

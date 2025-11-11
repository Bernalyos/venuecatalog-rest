package com.codeup.venuecatalog_rest.repository;

import com.codeup.venuecatalog_rest.entity.VenueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface VenueRepository extends JpaRepository<VenueEntity, Long> {

    boolean existsByName(String name);
}

package com.codeup.venuecatalog_rest.infraestructura.adapters.out.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VenueJpaRepository extends JpaRepository<VenueEntity, Long> {

        boolean existsByName(String name);
}

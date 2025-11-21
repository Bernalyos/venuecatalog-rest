package com.codeup.venuecatalog_rest.infraestructura.adapters.out.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventJpaRepository extends JpaRepository<EventEntity, Long> {

    boolean existsByName(String name);
}

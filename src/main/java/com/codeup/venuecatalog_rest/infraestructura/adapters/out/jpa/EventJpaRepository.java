package com.codeup.venuecatalog_rest.infraestructura.adapters.out.jpa;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;

public interface EventJpaRepository extends JpaRepository<EventEntity, Long>, JpaSpecificationExecutor<EventEntity> {

    @Override
    @EntityGraph(attributePaths = { "venue" })
    List<EventEntity> findAll();

    boolean existsByName(String name);
}

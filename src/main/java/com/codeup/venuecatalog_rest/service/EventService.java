package com.codeup.venuecatalog_rest.service;

import com.codeup.venuecatalog_rest.entity.EventEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

public interface EventService {

    EventEntity create(EventEntity event);
    EventEntity update(Long id, EventEntity event);
    void delete(Long id);
    Optional<EventEntity> findById(Long id);

    Page<EventEntity> findAll(Pageable pageable, String city, String category, LocalDate dateStart);
}

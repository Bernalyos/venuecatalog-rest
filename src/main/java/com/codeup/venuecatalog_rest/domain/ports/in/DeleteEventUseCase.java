package com.codeup.venuecatalog_rest.domain.ports.in;

import com.codeup.venuecatalog_rest.domain.model.Event;

import java.util.Optional;

public interface DeleteEventUseCase {
    void delete(Long id);
}

package com.codeup.venuecatalog_rest.domain.ports.in;

import com.codeup.venuecatalog_rest.domain.model.Event;

public interface UpdateEventUseCase {

    Event update(Long id, Event event);
}

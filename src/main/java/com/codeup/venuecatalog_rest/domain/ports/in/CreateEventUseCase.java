package com.codeup.venuecatalog_rest.domain.ports.in;

import com.codeup.venuecatalog_rest.domain.model.Event;

public interface CreateEventUseCase {

    Event create(Event event);
}

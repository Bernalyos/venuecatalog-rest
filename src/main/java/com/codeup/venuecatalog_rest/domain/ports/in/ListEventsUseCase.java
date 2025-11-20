package com.codeup.venuecatalog_rest.domain.ports.in;

import com.codeup.venuecatalog_rest.domain.model.Event;

import java.util.List;

public interface ListEventsUseCase {

    List<Event> list();
}

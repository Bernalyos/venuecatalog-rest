package com.codeup.venuecatalog_rest.aplication.usecase.EventUseCaseImpl;

import com.codeup.venuecatalog_rest.domain.model.Event;
import com.codeup.venuecatalog_rest.domain.ports.in.ListEventsUseCase;
import com.codeup.venuecatalog_rest.domain.ports.out.EventRepositoryPort;

import java.util.List;

public class ListEventsUseCaseImpl implements ListEventsUseCase {

    private final EventRepositoryPort eventRepositoryPort;

    public ListEventsUseCaseImpl(EventRepositoryPort eventRepositoryPort) {
        this.eventRepositoryPort = eventRepositoryPort;
    }

    @Override
    public List<Event> list() {
        return eventRepositoryPort.findAll();
    }
}

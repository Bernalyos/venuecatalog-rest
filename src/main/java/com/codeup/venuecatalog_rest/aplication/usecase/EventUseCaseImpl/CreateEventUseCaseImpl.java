package com.codeup.venuecatalog_rest.aplication.usecase.EventUseCaseImpl;

import com.codeup.venuecatalog_rest.domain.model.Event;
import com.codeup.venuecatalog_rest.domain.ports.in.CreateEventUseCase;
import com.codeup.venuecatalog_rest.domain.ports.out.EventRepositoryPort;

public class CreateEventUseCaseImpl implements CreateEventUseCase {

    private final EventRepositoryPort eventRepositoryPort;

    public CreateEventUseCaseImpl(EventRepositoryPort eventRepositoryPort) {
        this.eventRepositoryPort = eventRepositoryPort;
    }

    @Override
    public Event create(Event event) {
        return eventRepositoryPort.save(event);
    }
}


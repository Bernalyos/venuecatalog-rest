package com.codeup.venuecatalog_rest.aplication.usecase.EventUseCaseImpl;

import com.codeup.venuecatalog_rest.domain.model.Event;
import com.codeup.venuecatalog_rest.domain.ports.in.GetEventUseCase;
import com.codeup.venuecatalog_rest.domain.ports.out.EventRepositoryPort;

import java.util.Optional;

public class GetEventUseCaseImpl implements GetEventUseCase {

    private final EventRepositoryPort eventRepositoryPort;

    public GetEventUseCaseImpl(EventRepositoryPort eventRepositoryPort) {
        this.eventRepositoryPort = eventRepositoryPort;
    }

    @Override
    public Optional<Event> getById(Long id) {
        return eventRepositoryPort.findById(id);

    }
}

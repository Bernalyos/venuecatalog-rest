package com.codeup.venuecatalog_rest.aplication.usecase.EventUseCaseImpl;

import com.codeup.venuecatalog_rest.domain.model.Event;
import com.codeup.venuecatalog_rest.domain.ports.in.UpdateEventUseCase;
import com.codeup.venuecatalog_rest.domain.ports.out.EventRepositoryPort;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public class UpdateEventUseCaseImpl implements UpdateEventUseCase {

    private final EventRepositoryPort eventRepositoryPort;

    public UpdateEventUseCaseImpl(EventRepositoryPort eventRepositoryPort) {
        this.eventRepositoryPort = eventRepositoryPort;
    }

    @Override
    public Event update(Long id, Event event) {
        event.setId(id);
        return eventRepositoryPort.save(event);
    }
}

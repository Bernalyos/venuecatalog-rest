package com.codeup.venuecatalog_rest.aplication.usecase.EventUseCaseImpl;

import com.codeup.venuecatalog_rest.domain.ports.in.DeleteEventUseCase;
import com.codeup.venuecatalog_rest.domain.ports.out.EventRepositoryPort;

public class DeleteEventUseCaseImpl implements DeleteEventUseCase {

    private final EventRepositoryPort eventRepositoryPort;

    public DeleteEventUseCaseImpl(EventRepositoryPort eventRepositoryPort) {
        this.eventRepositoryPort = eventRepositoryPort;
    }

    @Override
    public void delete(Long id) {
        eventRepositoryPort.deleteById(id);
    }
}

package com.codeup.venuecatalog_rest.infraestructura.config;

import com.codeup.venuecatalog_rest.aplication.usecase.EventUseCaseImpl.*;
import com.codeup.venuecatalog_rest.aplication.usecase.EventUseCaseImpl.VenueUseCaseImpl.*;
import com.codeup.venuecatalog_rest.domain.ports.in.*;
import com.codeup.venuecatalog_rest.domain.ports.out.EventRepositoryPort;
import com.codeup.venuecatalog_rest.domain.ports.out.VenueRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    // EVENT BEANS
    @Bean
    public CreateEventUseCase createEventUseCase(EventRepositoryPort port) {
        return new CreateEventUseCaseImpl(port);
    }

    @Bean
    public GetEventUseCase getEventUseCase(EventRepositoryPort port) {
        return new GetEventUseCaseImpl(port);
    }

    @Bean
    public ListEventsUseCase listEventsUseCase(EventRepositoryPort port) {
        return new ListEventsUseCaseImpl(port);
    }

    @Bean
    public UpdateEventUseCase updateEventUseCase(EventRepositoryPort port) {
        return new UpdateEventUseCaseImpl(port);
    }

    @Bean
    public DeleteEventUseCase deleteEventUseCase(EventRepositoryPort port) {
        return new DeleteEventUseCaseImpl(port);
    }

    // VENUE BEANS
    @Bean
    public CreateVenueUseCase createVenueUseCase(VenueRepositoryPort port) {
        return new CreateVenueUseCaseImpl(port);
    }

    @Bean
    public GetVenueUseCase getVenueUseCase(VenueRepositoryPort port) {
        return new GetVenueUseCaseImpl(port);
    }

    @Bean
    public ListVenuesUseCase listVenuesUseCase(VenueRepositoryPort port) {
        return new ListVenuesUseCaseImpl(port);
    }

    @Bean
    public UpdateVenueUseCase updateVenueUseCase(VenueRepositoryPort port) {
        return new UpdateVenueUseCaseImpl(port);
    }

    @Bean
    public DeleteVenueUseCase deleteVenueUseCase(VenueRepositoryPort port) {
        return new DeleteVenueUseCaseImpl(port);
    }
}

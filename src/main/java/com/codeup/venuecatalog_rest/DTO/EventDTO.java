package com.codeup.venuecatalog_rest.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EventDTO {

    private long id;

    @NotBlank
    private String name;

    @NotNull
    private long venueId;

    private String description;

    public EventDTO(){}

    public EventDTO( long id, String name, long venueId, String description){

        this.id = id;
        this.name = name;
        this.venueId = venueId;
        this.description = description;

    }

      //metodos gett y sett

    public long getId(){ return id; }

    public void setId ( long id){ this.id = id;}

    public String getName(){ return name;}

    public void setName( String name){ this.name = name;}

    public long getVenueId(){ return venueId;}

    public void setVenueId( long venueId){ this.venueId = venueId;}

    public String getDescription(){ return description;}

    public void setDescription( String description){ this.description = description;}









}

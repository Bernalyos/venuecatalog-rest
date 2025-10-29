package com.codeup.venuecatalog_rest.DTO;

import jakarta.validation.constraints.NotBlank;

public class VenueDTO {

    private long id;

    @NotBlank
    private String name;

    private String address;

    public VenueDTO(){}

    public VenueDTO(long id, String name, String address ){

        this.id = id;
        this.name = name;
        this.address = address;

    }

    //metodos gett y sett

    public long getId(){ return id;}
    public void setId ( long id){ this.id = id;}

    public String getName(){ return name;}
    public void setName( String name){ this.name = name;}

    public String getAddress(){ return address;}
    public void setAddress(){ this.address = address;}
}

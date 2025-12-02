package com.codeup.venuecatalog_rest.domain.ports.in;

import com.codeup.venuecatalog_rest.domain.model.Venue;

import java.util.List;

public interface ListVenuesUseCase {
    List<Venue> list();
}

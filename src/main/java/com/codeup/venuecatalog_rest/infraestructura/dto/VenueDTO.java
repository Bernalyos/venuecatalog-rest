package com.codeup.venuecatalog_rest.infraestructura.dto;

import com.codeup.venuecatalog_rest.infraestructura.validation.ValidationGroups;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Venue
 * Includes comprehensive validation with groups for Create and Update
 * operations
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VenueDTO {

    @Null(groups = ValidationGroups.Create.class, message = "{venue.id.null}")
    @NotNull(groups = ValidationGroups.Update.class, message = "{venue.id.required}")
    @Positive(groups = ValidationGroups.Update.class, message = "{venue.id.positive}")
    private Long id;

    @NotBlank(message = "{venue.name.required}")
    @Size(min = 3, max = 100, message = "{venue.name.size}")
    private String name;

    @NotBlank(message = "{venue.location.required}")
    @Size(min = 5, max = 200, message = "{venue.location.size}")
    private String location;
}

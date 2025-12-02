package com.codeup.venuecatalog_rest.infraestructura.dto;

import com.codeup.venuecatalog_rest.infraestructura.validation.ValidDateRange;
import com.codeup.venuecatalog_rest.infraestructura.validation.ValidationGroups;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Data Transfer Object for Event
 * Includes comprehensive validation with groups for Create and Update
 * operations
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ValidDateRange(startDate = "startDate", endDate = "endDate")
public class EventDTO {

    @Null(groups = ValidationGroups.Create.class, message = "{event.id.null}")
    @NotNull(groups = ValidationGroups.Update.class, message = "{event.id.required}")
    @Positive(groups = ValidationGroups.Update.class, message = "{event.id.positive}")
    private Long id;

    @NotBlank(message = "{event.name.required}")
    @Size(min = 3, max = 100, message = "{event.name.size}")
    private String name;

    @NotBlank(message = "{event.description.required}")
    @Size(min = 10, max = 500, message = "{event.description.size}")
    private String description;

    @NotNull(message = "{event.date.required}")
    @Future(message = "{event.date.future}")
    private LocalDate startDate;

    @NotNull(message = "{event.date.required}")
    @Future(message = "{event.date.future}")
    private LocalDate endDate;

    @NotNull(message = "{event.venueId.required}")
    @Positive(message = "{event.venueId.positive}")
    private Long venueId;
}

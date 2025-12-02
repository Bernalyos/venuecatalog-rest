package com.codeup.venuecatalog_rest.infraestructura.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Validator implementation for ValidDateRange annotation
 * Validates that start date is before end date
 */
public class DateRangeValidator implements ConstraintValidator<ValidDateRange, Object> {

    private String startDateField;
    private String endDateField;

    @Override
    public void initialize(ValidDateRange constraintAnnotation) {
        this.startDateField = constraintAnnotation.startDate();
        this.endDateField = constraintAnnotation.endDate();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        BeanWrapper wrapper = new BeanWrapperImpl(value);
        Object startValue = wrapper.getPropertyValue(startDateField);
        Object endValue = wrapper.getPropertyValue(endDateField);

        if (startValue == null || endValue == null) {
            return true;
        }

        if (startValue instanceof LocalDate && endValue instanceof LocalDate) {
            LocalDate startDate = (LocalDate) startValue;
            LocalDate endDate = (LocalDate) endValue;
            return !startDate.isAfter(endDate);
        }

        if (startValue instanceof LocalDateTime && endValue instanceof LocalDateTime) {
            LocalDateTime startDateTime = (LocalDateTime) startValue;
            LocalDateTime endDateTime = (LocalDateTime) endValue;
            return !startDateTime.isAfter(endDateTime);
        }

        return true;
    }
}

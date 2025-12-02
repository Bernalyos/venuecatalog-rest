package com.codeup.venuecatalog_rest.infraestructura.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Global exception handler for the application
 * Implements RFC 7807 Problem Details for HTTP APIs
 * Provides standardized error responses with traceId and timestamp
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

        private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

        /**
         * Handles validation errors from @Valid annotation
         * Returns 400 Bad Request with field-level error details
         */
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ProblemDetail handleValidationException(
                        MethodArgumentNotValidException ex,
                        WebRequest request) {

                String traceId = getTraceId();

                ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                                HttpStatus.BAD_REQUEST,
                                "La validación de los datos de entrada ha fallado");

                problemDetail.setType(URI.create("https://api.venuecatalog.com/errors/validation-error"));
                problemDetail.setTitle("Error de Validación");
                problemDetail.setProperty("timestamp", Instant.now());
                problemDetail.setProperty("traceId", traceId);
                problemDetail.setProperty("instance", request.getDescription(false).replace("uri=", ""));

                // Extract field errors
                Map<String, String> errors = new HashMap<>();
                ex.getBindingResult().getAllErrors().forEach(error -> {
                        String fieldName = ((FieldError) error).getField();
                        String errorMessage = error.getDefaultMessage();
                        errors.put(fieldName, errorMessage);
                });

                problemDetail.setProperty("errors", errors);

                logger.error("Validation error - traceId: {}, errors: {}", traceId, errors);

                return problemDetail;
        }

        /**
         * Handles constraint violation exceptions
         * Returns 400 Bad Request with constraint violation details
         */
        @ExceptionHandler(ConstraintViolationException.class)
        public ProblemDetail handleConstraintViolationException(
                        ConstraintViolationException ex,
                        WebRequest request) {

                String traceId = getTraceId();

                ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                                HttpStatus.BAD_REQUEST,
                                "Violación de restricciones de validación");

                problemDetail.setType(URI.create("https://api.venuecatalog.com/errors/constraint-violation"));
                problemDetail.setTitle("Violación de Restricciones");
                problemDetail.setProperty("timestamp", Instant.now());
                problemDetail.setProperty("traceId", traceId);
                problemDetail.setProperty("instance", request.getDescription(false).replace("uri=", ""));

                Map<String, String> violations = ex.getConstraintViolations().stream()
                                .collect(Collectors.toMap(
                                                violation -> violation.getPropertyPath().toString(),
                                                ConstraintViolation::getMessage));

                problemDetail.setProperty("violations", violations);

                logger.error("Constraint violation - traceId: {}, violations: {}", traceId, violations);

                return problemDetail;
        }

        /**
         * Handles entity not found exceptions
         * Returns 404 Not Found
         */
        @ExceptionHandler(EntityNotFoundException.class)
        public ProblemDetail handleEntityNotFoundException(
                        EntityNotFoundException ex,
                        WebRequest request) {

                String traceId = getTraceId();

                ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                                HttpStatus.NOT_FOUND,
                                ex.getMessage());

                problemDetail.setType(URI.create("https://api.venuecatalog.com/errors/entity-not-found"));
                problemDetail.setTitle("Entidad No Encontrada");
                problemDetail.setProperty("timestamp", Instant.now());
                problemDetail.setProperty("traceId", traceId);
                problemDetail.setProperty("instance", request.getDescription(false).replace("uri=", ""));

                logger.error("Entity not found - traceId: {}, message: {}", traceId, ex.getMessage());

                return problemDetail;
        }

        /**
         * Handles data integrity violation exceptions
         * Returns 409 Conflict
         */
        @ExceptionHandler(DataIntegrityViolationException.class)
        public ProblemDetail handleDataIntegrityViolationException(
                        DataIntegrityViolationException ex,
                        WebRequest request) {

                String traceId = getTraceId();

                ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                                HttpStatus.CONFLICT,
                                "Violación de integridad de datos. Posible duplicado o restricción de clave foránea.");

                problemDetail.setType(URI.create("https://api.venuecatalog.com/errors/data-integrity-violation"));
                problemDetail.setTitle("Violación de Integridad de Datos");
                problemDetail.setProperty("timestamp", Instant.now());
                problemDetail.setProperty("traceId", traceId);
                problemDetail.setProperty("instance", request.getDescription(false).replace("uri=", ""));

                logger.error("Data integrity violation - traceId: {}, message: {}",
                                traceId, ex.getMostSpecificCause().getMessage());

                return problemDetail;
        }

        /**
         * Handles illegal argument exceptions
         * Returns 400 Bad Request
         */
        @ExceptionHandler(IllegalArgumentException.class)
        public ProblemDetail handleIllegalArgumentException(
                        IllegalArgumentException ex,
                        WebRequest request) {

                String traceId = getTraceId();

                ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                                HttpStatus.BAD_REQUEST,
                                ex.getMessage());

                problemDetail.setType(URI.create("https://api.venuecatalog.com/errors/illegal-argument"));
                problemDetail.setTitle("Argumento Inválido");
                problemDetail.setProperty("timestamp", Instant.now());
                problemDetail.setProperty("traceId", traceId);
                problemDetail.setProperty("instance", request.getDescription(false).replace("uri=", ""));

                logger.error("Illegal argument - traceId: {}, message: {}", traceId, ex.getMessage());

                return problemDetail;
        }

        /**
         * Handles all other exceptions
         * Returns 500 Internal Server Error
         */
        @ExceptionHandler(Exception.class)
        public ProblemDetail handleGlobalException(
                        Exception ex,
                        WebRequest request) {

                String traceId = getTraceId();

                ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "Ha ocurrido un error interno en el servidor");

                problemDetail.setType(URI.create("https://api.venuecatalog.com/errors/internal-server-error"));
                problemDetail.setTitle("Error Interno del Servidor");
                problemDetail.setProperty("timestamp", Instant.now());
                problemDetail.setProperty("traceId", traceId);
                problemDetail.setProperty("instance", request.getDescription(false).replace("uri=", ""));

                logger.error("Internal server error - traceId: {}, exception: {}, message: {}",
                                traceId, ex.getClass().getSimpleName(), ex.getMessage(), ex);

                return problemDetail;
        }

        /**
         * Retrieves the trace ID from MDC
         */
        private String getTraceId() {
                String traceId = org.slf4j.MDC.get("traceId");
                return traceId != null ? traceId : UUID.randomUUID().toString();
        }
}

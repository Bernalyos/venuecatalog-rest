# VenueCatalog REST

Proyecto Spring Boot que expone una API para gestionar recintos (venues) y eventos (events) con persistencia JPA (H2 en memoria), validaciones, paginación y filtros.

## Resumen
- **Entidades**: `EventEntity`, `VenueEntity` (JPA)
- **Repositorios**: `EventRepository`, `VenueRepository` (`JpaRepository` + `JpaSpecificationExecutor`)
- **Servicios**: `EventService`, `VenueService` con implementación persistente
- **DTOs**: `EventDto`, `VenueDto` y mappers (`EventMapper`, `VenueMapper`)
- **Controladores REST**: `EventController` y `VenueController` (usa DTOs)
- **Manejo global de errores**: `GlobalExceptionHandler` (JSON estructurado)
- **Base de datos**: H2 in-memory para desarrollo y pruebas
- **Documentación**: Swagger/OpenAPI mediante `springdoc-openapi`

## Requisitos
- JDK 17+ (proyecto configurado para Java 17 en `pom.xml`)
- Maven (se incluye wrapper `./mvnw`)

## Ejecutar la aplicación

Desde la raíz del proyecto:

```bash
./mvnw spring-boot:run
```

O construir y ejecutar el JAR:

```bash
./mvnw -DskipTests clean package
java -jar target/venuecatalog-rest-0.0.1-SNAPSHOT.jar
```

Por defecto la aplicación corre en `http://localhost:8080`.

## Documentación de la API (Swagger / OpenAPI)

Puedes acceder a la documentación interactiva de la API y probar los endpoints directamente desde el navegador:

- **Swagger UI**: [http://localhost:8080/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/index.html#/)
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

Si no ves Swagger:
1. Asegúrate que la app está arrancada y sin errores en consola.
2. Revisa que `org.springdoc:springdoc-openapi-starter-webmvc-ui` esté en `pom.xml`.

## H2 Console

Para inspeccionar la base de datos en memoria:
- **URL**: `http://localhost:8080/h2-console`
- **JDBC URL**: `jdbc:h2:mem:testdb` (Configurado en `application.properties`)
- **Usuario**: `sa`
- **Contraseña**: (dejar vacía)

## Endpoints principales

Base: `/api`

### Venues
- `GET /api/venues` — Lista venues
- `GET /api/venues/{id}` — Obtener venue por id
- `POST /api/venues` — Crear venue (payload `VenueDto`)
- `DELETE /api/venues/{id}` — Borrar venue

### Events
- `GET /api/events` — Lista paginada de eventos. Parámetros opcionales: `page`, `size`, `sort`, `city`, `category`, `dateStart`.
- `GET /api/events/{id}` — Obtener evento por id
- `POST /api/events` — Crear evento (payload `EventDto`)
- `PUT /api/events/{id}` — Actualizar evento (payload `EventDto`)
- `DELETE /api/events/{id}` — Borrar evento

## Ejemplos de uso (cURL)

**Crear un Venue:**
```bash
curl -X POST http://localhost:8080/api/venues \
  -H "Content-Type: application/json" \
  -d '{"name":"Sala Moliere","city":"Paris"}'
```

**Crear un Evento:**
```bash
curl -X POST http://localhost:8080/api/events \
  -H "Content-Type: application/json" \
  -d '{"name":"Concierto de Jazz","description":"Noche de jazz","dateStart":"2025-12-01T20:00:00","venueId":1,"category":"music"}'
```

## Tests

Para ejecutar las pruebas unitarias y de integración:

```bash
./mvnw test
```

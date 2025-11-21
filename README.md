# VenueCatalog REST

Proyecto Spring Boot que expone una API para gestionar recintos (venues) y eventos (events) con persistencia JPA (H2 en memoria), validaciones, paginación y filtros.

## Resumen
- Entidades: `EventEntity`, `VenueEntity` (JPA)
- Repositorios: `EventRepository`, `VenueRepository` (`JpaRepository` + `JpaSpecificationExecutor`)
- Servicios: `EventService`, `VenueService` con implementación persistente
- DTOs: `EventDto`, `VenueDto` y mappers (`EventMapper`, `VenueMapper`)
- Controladores REST: `EventController` y `VenueController` (usa DTOs)
- Manejo global de errores: `GlobalExceptionHandler` (JSON estructurado)
- H2 in-memory para desarrollo y pruebas
- Swagger/OpenAPI mediante `springdoc-openapi` (UI disponible)

## Requisitos
- JDK 17+ (proyecto configurado para Java 17 en `pom.xml`)
- Maven (se incluye wrapper `./mvnw`)

Si quieres actualizar a Java 21 se requiere cambiar la configuración del `pom.xml` y usar JDK 21 en la máquina.


### Estructura de Carpetas

```text
src/main/java/com/codeup/venuecatalog_rest
├── aplication
│   └── usecase          # Implementación de casos de uso (Lógica de negocio)
├── domain
│   ├── model            # Objetos de dominio puro (Entidades de negocio)
│   └── ports            # Interfaces (Puertos de entrada y salida)
└── infraestructura
    ├── adapters
    │   ├── in
    │   │   └── web      # Controladores REST (Entrada)
    │   └── out
    │       └── jpa      # Adaptadores de persistencia Spring Data JPA (Salida)
    ├── config           # Configuración de Beans (Inyección de dependencias)
    ├── dto              # Data Transfer Objects (API)
    └── mappers          # Mappers (MapStruct)
```

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

Por defecto corre en `http://localhost:8080`.

## Swagger / OpenAPI
- Swagger UI: `http://localhost:8080/swagger-ui.html` o `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

Si no ves Swagger:
- Asegúrate que la app está arrancada y sin errores en consola.
- Revisa que `org.springdoc:springdoc-openapi-starter-webmvc-ui` esté en `pom.xml` (ya incluido).
- Si aplicas seguridad (Spring Security) recuerda permitir el acceso a `/swagger-ui/**` y `/v3/api-docs/**`.

## H2 Console
- URL: `http://localhost:8080/h2-console`
- JDBC URL (por defecto en `application.properties`): `jdbc:h2:mem:venuecatalog`
- Usuario: `sa` — contraseña vacía

## Endpoints principales

Base: `/api`

- Venues
  - `GET /api/venues` — Lista venues
  - `GET /api/venues/{id}` — Obtener venue por id
  - `POST /api/venues` — Crear venue (payload `VenueDto`)
  - `DELETE /api/venues/{id}` — Borrar venue

- Events
  - `GET /api/events` — Lista paginada de eventos. Parámetros opcionales de query: `page`, `size`, `sort`, `city`, `category`, `dateStart` (ISO date). Ejemplo:
    - `/api/events?page=0&size=10&sort=name,asc&city=Paris&category=music&dateStart=2025-11-11`
  - `GET /api/events/{id}` — Obtener evento por id
  - `POST /api/events` — Crear evento (payload `EventDto`)
  - `PUT /api/events/{id}` — Actualizar evento (payload `EventDto`)
  - `DELETE /api/events/{id}` — Borrar evento

## DTOs (ejemplos)

`VenueDto` (JSON):

```json
{
  "id": null,
  "name": "Sala Moliere",
  "city": "Paris"
}
```

`EventDto` (JSON):

```json
{
  "id": null,
  "name": "Concierto de Jazz",
  "description": "Noche de jazz",
  "dateStart": "2025-12-01T20:00:00",
  "venueId": 1,
  "category": "music"
}
```

Nota: `venueId` es opcional en creación, pero si se indica el ID debe existir (se validará y retornará 404 si no existe).

## Ejemplos curl

- Crear un Venue:
```bash
curl -X POST http://localhost:8080/api/venues \
  -H "Content-Type: application/json" \
  -d '{"name":"Sala Moliere","city":"Paris"}'
```

- Crear un Event:
```bash
curl -X POST http://localhost:8080/api/events \
  -H "Content-Type: application/json" \
  -d '{"name":"Concierto de Jazz","description":"Noche de jazz","dateStart":"2025-12-01T20:00:00","venueId":1,"category":"music"}'
```

- Listar eventos con paginación y filtros:
```bash
curl "http://localhost:8080/api/events?page=0&size=10&city=Paris&category=music"
```

## Manejo de errores

Se devuelve JSON estructurado por el `GlobalExceptionHandler`. Ejemplo de respuesta de validación 400:

```json
{
  "timestamp": "2025-11-11T12:34:56.789",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "uri=/api/events",
  "details": ["name: Event name is required", "dateStart: must be a future date"]
}
```

Respuesta de conflicto (409) al intentar crear un evento con nombre duplicado:

```json
{
  "timestamp": "2025-11-11T12:34:56.789",
  "status": 409,
  "error": "Conflict",
  "message": "An event with this name already exists",
  "path": "uri=/api/events"
}
```

## Validaciones importantes
- `EventEntity`:
  - `name`: `@NotBlank`, `@Size(max=150)` — obligatorio y único
  - `description`: `@Size(max=1000)`
  - `dateStart`: `@NotNull`, `@Future`
  - `category`: `@NotBlank`
- `VenueEntity`:
  - `name`: `@NotBlank`, `@Size(max=150)`
  - `city`: `@NotBlank`, `@Size(max=100)`

## Tests

Para ejecutar pruebas (si existen):

```bash
./mvnw test
```

## Notas finales y próximos pasos sugeridos
- Se recomiendan tests de integración para verificar paginación y filtros.
- Mantener DTOs y mappers para evitar exponer entidades JPA directas en la API.
- Considerar habilitar CORS o configuración de seguridad si se exponen en producción.

Si quieres, puedo:
- Añadir tests de integración para `/api/events` (paginación, filtros y validaciones).
- Crear un script de ejemplo que cargue datos iniciales (data.sql) para pruebas.
- Empujar un commit final con estos cambios si quieres que lo haga.

---
Generated by the project assistant — instrucciones en español y ejemplos para arrancar y probar la API.
# venuecatalog-rest
Implementation of a REST API with Spring Boot for managing an event and venue catalog using in-memory storage. Includes validation, error handling, environment profiling, and OpenAPI documentation (Swagger UI).

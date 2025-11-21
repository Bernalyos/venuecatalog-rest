# VenueCatalog REST

Proyecto Spring Boot que expone una API para gestionar recintos (venues) y eventos (events) con persistencia JPA (H2 en memoria), validaciones, paginación, filtros y migraciones de base de datos.

## Resumen
- **Gestión de Venues y Eventos**: CRUD completo con validaciones.
- **Relaciones JPA**: Relación `OneToMany` / `ManyToOne` entre Venues y Events con gestión de ciclo de vida.
- **Optimización de Consultas**: Búsquedas dinámicas con Specifications y prevención de N+1 queries con `@EntityGraph`.
- **Transaccionalidad**: Gestión de transacciones con `@Transactional` en la capa de aplicación.
- **Migraciones**: Flyway para control de versiones del esquema de base de datos.
- **Persistencia**: Base de datos H2 en memoria con esquema gestionado por Flyway.
- **API REST**: Endpoints documentados con OpenAPI.
- **Arquitectura**: Diseño hexagonal para mantenibilidad y desacoplamiento.
- **Documentación**: Swagger/OpenAPI mediante `springdoc-openapi` (UI disponible)

## Arquitectura

El proyecto sigue una **Arquitectura Hexagonal** (Ports and Adapters) para desacoplar la lógica de negocio de los detalles de infraestructura y frameworks.

### Estructura de Carpetas

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

## Características Principales

### 1. Relaciones JPA

El proyecto implementa relaciones bidireccionales entre `VenueEntity` y `EventEntity`:

- **`VenueEntity`**: Contiene una colección de eventos (`@OneToMany`)
  - `cascade = CascadeType.ALL`: Al guardar/eliminar un venue, se guardan/eliminan sus eventos
  - `orphanRemoval = true`: Los eventos huérfanos se eliminan automáticamente
  - `fetch = FetchType.LAZY`: Los eventos se cargan bajo demanda

- **`EventEntity`**: Referencia a su venue (`@ManyToOne`)
  - `fetch = FetchType.LAZY`: El venue se carga bajo demanda
  - `@JoinColumn(name = "venue_id")`: Clave foránea en la tabla `events`

**Métodos helper** en `VenueEntity` para mantener consistencia bidireccional:
```java
venue.addEvent(event);    // Agrega evento y establece la relación bidireccional
venue.removeEvent(event); // Remueve evento y limpia la relación
```

### 2. Optimización de Consultas

#### Búsquedas Dinámicas con Specifications
El repositorio `EventJpaRepository` extiende `JpaSpecificationExecutor` para permitir consultas dinámicas:

```java
// Búsqueda con filtros opcionales
List<Event> events = eventAdapter.search("Concert", LocalDate.of(2023, 10, 1), venueId);
```

Filtros disponibles:
- **name**: Búsqueda parcial (case-insensitive)
- **date**: Filtro por fecha exacta
- **venueId**: Filtro por venue

#### Prevención de N+1 Queries
Se utiliza `@EntityGraph` para cargar relaciones de forma eficiente:

```java
@EntityGraph(attributePaths = {"venue"})
List<EventEntity> findAll();
```

Esto garantiza que al listar eventos, los venues asociados se cargan en una sola consulta SQL.

### 3. Transaccionalidad

Todos los casos de uso en la capa de aplicación están anotados con `@Transactional`:

- **Operaciones de escritura** (`Create`, `Update`, `Delete`): `@Transactional`
- **Operaciones de lectura** (`Get`, `List`): `@Transactional(readOnly = true)`

Esto asegura:
- Consistencia de datos
- Rollback automático en caso de errores
- Optimizaciones de rendimiento para consultas de solo lectura

### 4. Migraciones con Flyway

El esquema de base de datos se gestiona mediante scripts de migración versionados:

**Ubicación**: `src/main/resources/db/migration/`

- **`V1__init.sql`**: Creación de tablas `venues` y `events` con clave foránea
- **`V2__data.sql`**: Datos iniciales de prueba

**Configuración**:
- `spring.jpa.hibernate.ddl-auto=validate`: Hibernate valida el esquema sin modificarlo
- Flyway ejecuta automáticamente las migraciones pendientes al iniciar la aplicación

**Verificar migraciones**:
```sql
-- En H2 Console
SELECT * FROM flyway_schema_history;
```

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

### Tests de Integración

El proyecto incluye tests de integración que verifican:

**`RelationshipIntegrationTest`**:
- Relaciones bidireccionales entre Venue y Event
- Cascade de operaciones (guardar/eliminar)
- Orphan removal
- Lazy loading

**`QueryOptimizationTest`**:
- Búsquedas dinámicas con filtros (nombre, fecha, venue)
- Filtros combinados
- EntityGraph para prevención de N+1 queries

## Tecnologías Utilizadas

- **Spring Boot 3.5.7**: Framework principal
- **Spring Data JPA**: Persistencia y repositorios
- **Hibernate**: ORM
- **Flyway**: Migraciones de base de datos
- **H2 Database**: Base de datos en memoria
- **MapStruct 1.5.5**: Mapeo de objetos
- **SpringDoc OpenAPI**: Documentación de API
- **JUnit 5**: Testing

## Estructura de Commits

El proyecto sigue buenas prácticas de Git:

- **Ramas de feature**: Cada tarea se desarrolló en su propia rama
  - `feature/task1-relationships`: Relaciones JPA
  - `feature/task2-optimization`: Optimización de consultas
  - `feature/task3-transactions`: Transaccionalidad y Flyway
- **Rama de integración**: `HU-semana4` contiene todos los cambios integrados
- **Commits descriptivos**: Mensajes claros siguiendo convención `Feat:`, `Fix:`, etc.

## Autor

Proyecto desarrollado como parte del curso de Spring Boot - Arquitectura Hexagonal.


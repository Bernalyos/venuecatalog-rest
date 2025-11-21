# VenueCatalog REST

Spring Boot project that exposes an API to manage venues and events with JPA persistence (H2 in-memory database), validations, pagination, filters, and database migrations.

## Summary
- **Venue and Event Management**: Complete CRUD with validations.
- **JPA Relationships**: `OneToMany` / `ManyToOne` relationship between Venues and Events with lifecycle management.
- **Query Optimization**: Dynamic searches with Specifications and N+1 query prevention with `@EntityGraph`.
- **Transactionality**: Transaction management with `@Transactional` in the application layer.
- **Migrations**: Flyway for database schema version control.
- **Persistence**: H2 in-memory database with schema managed by Flyway.
- **REST API**: Endpoints documented with OpenAPI.
- **Architecture**: Hexagonal design for maintainability and decoupling.
- **Documentation**: Swagger/OpenAPI via `springdoc-openapi` (UI available)

## Architecture

The project follows a **Hexagonal Architecture** (Ports and Adapters) to decouple business logic from infrastructure details and frameworks.

### Folder Structure

### Folder Structure

```text
src/main/java/com/codeup/venuecatalog_rest
├── aplication
│   └── usecase          # Use case implementations (Business logic)
├── domain
│   ├── model            # Pure domain objects (Business entities)
│   └── ports            # Interfaces (Input and output ports)
└── infraestructura
    ├── adapters
    │   ├── in
    │   │   └── web      # REST Controllers (Input)
    │   └── out
    │       └── jpa      # Spring Data JPA persistence adapters (Output)
    ├── config           # Bean configuration (Dependency injection)
    ├── dto              # Data Transfer Objects (API)
    └── mappers          # Mappers (MapStruct)
```

## Requirements
- JDK 17+ (project configured for Java 17 in `pom.xml`)
- Maven (wrapper `./mvnw` included)

## Running the Application

From the project root:

```bash
./mvnw spring-boot:run
```

Or build and run the JAR:

```bash
./mvnw -DskipTests clean package
java -jar target/venuecatalog-rest-0.0.1-SNAPSHOT.jar
```

By default, the application runs on `http://localhost:8080`.

## Docker Support

The application can be run using Docker for consistent deployment across environments.

### Quick Start with Docker Compose

```bash
# Build and start the application
docker-compose up

# Or run in background
docker-compose up -d

# View logs
docker-compose logs -f

# Stop the application
docker-compose down
```

### Manual Docker Commands

```bash
# Build the image
docker build -t venuecatalog-rest:latest .

# Run the container
docker run -p 8080:8080 --name venuecatalog venuecatalog-rest:latest
```

### Docker Features

- **Multi-stage build**: Optimized image size (~200MB)
- **Health checks**: Automatic monitoring
- **Environment variables**: Easy configuration
- **Layer caching**: Fast rebuilds

For detailed Docker documentation, see [DOCKER.md](DOCKER.md).

## API Documentation (Swagger / OpenAPI)

You can access the interactive API documentation and test endpoints directly from your browser:

- **Swagger UI**: [http://localhost:8080/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/index.html#/)
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

If you don't see Swagger:
1. Make sure the app is running without errors in the console.
2. Check that `org.springdoc:springdoc-openapi-starter-webmvc-ui` is in `pom.xml`.

## H2 Console

To inspect the in-memory database:
- **URL**: `http://localhost:8080/h2-console`
- **JDBC URL**: `jdbc:h2:mem:testdb` (Configured in `application.properties`)
- **Username**: `sa`
- **Password**: (leave empty)

## Key Features

### 1. JPA Relationships

The project implements bidirectional relationships between `VenueEntity` and `EventEntity`:

- **`VenueEntity`**: Contains a collection of events (`@OneToMany`)
  - `cascade = CascadeType.ALL`: When saving/deleting a venue, its events are saved/deleted
  - `orphanRemoval = true`: Orphaned events are automatically deleted
  - `fetch = FetchType.LAZY`: Events are loaded on demand

- **`EventEntity`**: References its venue (`@ManyToOne`)
  - `fetch = FetchType.LAZY`: Venue is loaded on demand
  - `@JoinColumn(name = "venue_id")`: Foreign key in the `events` table

**Helper methods** in `VenueEntity` to maintain bidirectional consistency:
```java
venue.addEvent(event);    // Adds event and establishes bidirectional relationship
venue.removeEvent(event); // Removes event and clears relationship
```

### 2. Query Optimization

#### Dynamic Searches with Specifications
The `EventJpaRepository` extends `JpaSpecificationExecutor` to allow dynamic queries:

```java
// Search with optional filters
List<Event> events = eventAdapter.search("Concert", LocalDate.of(2023, 10, 1), venueId);
```

Available filters:
- **name**: Partial search (case-insensitive)
- **date**: Filter by exact date
- **venueId**: Filter by venue

#### N+1 Query Prevention
`@EntityGraph` is used to load relationships efficiently:

```java
@EntityGraph(attributePaths = {"venue"})
List<EventEntity> findAll();
```

This ensures that when listing events, associated venues are loaded in a single SQL query.

### 3. Transactionality

All use cases in the application layer are annotated with `@Transactional`:

- **Write operations** (`Create`, `Update`, `Delete`): `@Transactional`
- **Read operations** (`Get`, `List`): `@Transactional(readOnly = true)`

This ensures:
- Data consistency
- Automatic rollback on errors
- Performance optimizations for read-only queries

### 4. Flyway Migrations

The database schema is managed through versioned migration scripts:

**Location**: `src/main/resources/db/migration/`

- **`V1__init.sql`**: Creation of `venues` and `events` tables with foreign key
- **`V2__data.sql`**: Initial test data

**Configuration**:
- `spring.jpa.hibernate.ddl-auto=validate`: Hibernate validates the schema without modifying it
- Flyway automatically executes pending migrations on application startup

**Verify migrations**:
```sql
-- In H2 Console
SELECT * FROM flyway_schema_history;
```

## Main Endpoints

Base: `/api`

### Venues
- `GET /api/venues` — List venues
- `GET /api/venues/{id}` — Get venue by id
- `POST /api/venues` — Create venue (payload `VenueDto`)
- `DELETE /api/venues/{id}` — Delete venue

### Events
- `GET /api/events` — Paginated list of events. Optional parameters: `page`, `size`, `sort`, `city`, `category`, `dateStart`.
- `GET /api/events/{id}` — Get event by id
- `POST /api/events` — Create event (payload `EventDto`)
- `PUT /api/events/{id}` — Update event (payload `EventDto`)
- `DELETE /api/events/{id}` — Delete event

## Usage Examples (cURL)

**Create a Venue:**
```bash
curl -X POST http://localhost:8080/api/venues \
  -H "Content-Type: application/json" \
  -d '{"name":"Sala Moliere","city":"Paris"}'
```

**Create an Event:**
```bash
curl -X POST http://localhost:8080/api/events \
  -H "Content-Type: application/json" \
  -d '{"name":"Jazz Concert","description":"Jazz night","dateStart":"2025-12-01T20:00:00","venueId":1,"category":"music"}'
```

## Tests

To run unit and integration tests:

```bash
./mvnw test
```

### Integration Tests

The project includes integration tests that verify:

**`RelationshipIntegrationTest`**:
- Bidirectional relationships between Venue and Event
- Cascade operations (save/delete)
- Orphan removal
- Lazy loading

**`QueryOptimizationTest`**:
- Dynamic searches with filters (name, date, venue)
- Combined filters
- EntityGraph for N+1 query prevention

## Technologies Used

- **Spring Boot 3.5.7**: Main framework
- **Spring Data JPA**: Persistence and repositories
- **Hibernate**: ORM
- **Flyway**: Database migrations
- **H2 Database**: In-memory database
- **MapStruct 1.5.5**: Object mapping
- **SpringDoc OpenAPI**: API documentation
- **JUnit 5**: Testing

## Commit Structure

The project follows Git best practices:

- **Feature branches**: Each task was developed in its own branch
  - `feature/task1-relationships`: JPA relationships
  - `feature/task2-optimization`: Query optimization
  - `feature/task3-transactions`: Transactionality and Flyway
- **Integration branch**: `HU-semana4` contains all integrated changes
- **Descriptive commits**: Clear messages following `Feat:`, `Fix:`, etc. convention

## Author

Project developed as part of the Spring Boot - Hexagonal Architecture course.

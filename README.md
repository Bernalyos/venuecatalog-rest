# VenueCatalog REST API

Sistema de gestión de eventos y venues con arquitectura hexagonal, seguridad JWT, logging estructurado y control de acceso basado en roles.

## 📋 Características Principales

- **Gestión de Venues y Eventos**: CRUD completo con validaciones avanzadas
- **Seguridad JWT**: Autenticación stateless con tokens firmados
- **Control de Acceso por Rol**: RBAC con `@PreAuthorize`
- **Logging Estructurado**: Trazabilidad con `traceId` en cada request
- **Manejo de Errores**: RFC 7807 ProblemDetail con contexto completo
- **CORS Configurado**: Listo para integración con frontends
- **Optimización de Queries**: Prevención N+1 con `@EntityGraph`
- **Migraciones de BD**: Flyway para control de versiones del schema
- **Documentación API**: Swagger/OpenAPI interactivo

## 🏗️ Arquitectura

Arquitectura Hexagonal (Puertos y Adaptadores) para desacoplar lógica de negocio de infraestructura.

```text
src/main/java/com/codeup/venuecatalog_rest
├── aplication
│   ├── service          # Servicios de aplicación (AuthService)
│   └── usecase          # Casos de uso (Lógica de negocio)
├── domain
│   ├── model            # Entidades de dominio puras
│   └── ports            # Interfaces (Puertos de entrada/salida)
└── infraestructura
    ├── adapters
    │   ├── in
    │   │   └── web      # Controladores REST
    │   └── out
    │       └── jpa      # Adaptadores de persistencia
    ├── config           # Configuración (Security, Beans)
    ├── dto              # Data Transfer Objects
    ├── exception        # Manejo global de excepciones
    ├── mappers          # MapStruct mappers
    ├── security         # JWT, Filters, UserDetailsService
    └── validation       # Grupos de validación
```

## 🚀 Inicio Rápido

### Requisitos
- JDK 17+
- Maven 3.6+
- Docker (opcional)

### Ejecución Local

```bash
# Con Maven wrapper
./mvnw spring-boot:run

# O construir JAR
./mvnw clean package -DskipTests
java -jar target/venuecatalog-rest-0.0.1-SNAPSHOT.jar
```

### Con Docker Compose

```bash
# Iniciar aplicación y base de datos
docker-compose up -d

# Ver logs
docker-compose logs -f

# Detener
docker-compose down
```

La aplicación estará disponible en `http://localhost:8080`

## 🔐 Autenticación y Seguridad

### Registro de Usuario

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "usuario",
    "password": "password123"
  }'
```

**Respuesta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### Login

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "usuario",
    "password": "password123"
  }'
```

### Uso del Token

```bash
curl http://localhost:8080/venues \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

### Roles y Permisos

- **USER**: Acceso de lectura (GET)
- **ADMIN**: Acceso completo (GET, POST, PUT, DELETE)

Por defecto, los usuarios registrados tienen rol `USER`. Para promover a `ADMIN`:

```sql
-- Conectar a la base de datos
UPDATE users SET role = 'ADMIN' WHERE username = 'usuario';
```

## 📡 Endpoints Principales

### Autenticación (Público)
- `POST /auth/register` — Registro de usuario
- `POST /auth/login` — Inicio de sesión

### Venues (Requiere autenticación)
- `GET /venues` — Listar venues (USER/ADMIN)
- `GET /venues/{id}` — Obtener venue (USER/ADMIN)
- `POST /venues` — Crear venue (ADMIN)
- `PUT /venues/{id}` — Actualizar venue (ADMIN)
- `DELETE /venues/{id}` — Eliminar venue (ADMIN)

### Events (Requiere autenticación)
- `GET /events` — Listar eventos (USER/ADMIN)
- `GET /events/{id}` — Obtener evento (USER/ADMIN)
- `POST /events` — Crear evento (ADMIN)
- `PUT /events/{id}` — Actualizar evento (ADMIN)
- `DELETE /events/{id}` — Eliminar evento (ADMIN)

## 📖 Documentación API

### Swagger UI
Accede a la documentación interactiva en:
- **URL**: http://localhost:8080/swagger-ui/index.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

### Ejemplos de Uso

**Crear Venue (requiere rol ADMIN):**
```bash
curl -X POST http://localhost:8080/venues \
  -H "Authorization: Bearer <ADMIN_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Estadio Nacional",
    "location": "Lima",
    "capacity": 50000
  }'
```

**Crear Evento (requiere rol ADMIN):**
```bash
curl -X POST http://localhost:8080/events \
  -H "Authorization: Bearer <ADMIN_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Concierto de Rock",
    "description": "Gran evento musical",
    "date": "2025-12-25T20:00:00",
    "venueId": 1
  }'
```

## 🔍 Logging y Observabilidad

Todos los logs incluyen contexto estructurado:

```
2025-12-02 14:53:40.670 INFO [nio-8080-exec-1] [traceId=2208aba1-117e-4d9f-93f5-d90a8b489b0e] [user=testuser] [GET /venues]
```

- **traceId**: Identificador único para rastrear requests
- **user**: Usuario autenticado (o "anonymous")
- **method**: Método HTTP
- **uri**: Endpoint accedido

### Manejo de Errores

Todos los errores siguen RFC 7807 (ProblemDetail):

```json
{
  "type": "https://api.venuecatalog.com/errors/access-denied",
  "title": "Acceso Denegado",
  "status": 403,
  "detail": "Acceso denegado. No tienes permisos para realizar esta acción.",
  "instance": "/venues",
  "timestamp": "2025-12-02T19:56:56.821674255Z",
  "traceId": "b9ab36f7-0a02-44e7-a2a7-c79035ff7431"
}
```

## 🌐 Configuración CORS

CORS está configurado para permitir requests desde:
- `http://localhost:3000` (React)
- `http://localhost:4200` (Angular)
- `http://localhost:8081` (Alternativo)
- `http://localhost:5173` (Vite)

**Métodos permitidos**: GET, POST, PUT, DELETE, PATCH, OPTIONS  
**Headers expuestos**: Authorization, X-Trace-Id  
**Credenciales**: Habilitadas

## 🗄️ Base de Datos

### MySQL (Producción/Docker)
- **Host**: localhost:3307
- **Database**: venuecatalog
- **User**: user
- **Password**: password

### H2 Console (Desarrollo)
- **URL**: http://localhost:8080/h2-console
- **JDBC URL**: `jdbc:mysql://localhost:3307/venuecatalog`
- **Username**: user
- **Password**: password

### Migraciones Flyway

Ubicación: `src/main/resources/db/migration/`

- **V1__init.sql**: Tablas venues y events
- **V2__data.sql**: Datos iniciales
- **V3__security.sql**: Tabla users

Verificar migraciones:
```sql
SELECT * FROM flyway_schema_history;
```

## 🧪 Tests

```bash
# Ejecutar todos los tests
./mvnw test

# Tests específicos
./mvnw test -Dtest=RelationshipIntegrationTest
./mvnw test -Dtest=QueryOptimizationTest
```

### Tests Incluidos
- **RelationshipIntegrationTest**: Relaciones JPA y cascadas
- **QueryOptimizationTest**: Búsquedas dinámicas y EntityGraph
- **SecurityTests**: Autenticación y autorización

## 🛠️ Tecnologías

- **Spring Boot 3.5.7**: Framework principal
- **Spring Security**: Autenticación y autorización
- **JWT (jjwt 0.11.5)**: Tokens de autenticación
- **Spring Data JPA**: Persistencia
- **Hibernate**: ORM
- **Flyway**: Migraciones de BD
- **MySQL 8.0**: Base de datos
- **MapStruct 1.5.5**: Mapeo de objetos
- **SpringDoc OpenAPI**: Documentación API
- **Logback**: Logging estructurado
- **JUnit 5 & Mockito**: Testing

## 📦 Estructura de Commits

- **feature/task2-observability-security**: Logging estructurado y seguridad JWT
- **feature/task3-cors-security-policies**: Configuración CORS

Convención de commits: `feat:`, `fix:`, `docs:`, `refactor:`

## 🔧 Configuración

### Variables de Entorno (application.properties)

```properties
# JWT
application.security.jwt.secret-key=<tu-clave-secreta-256-bits>
application.security.jwt.expiration=86400000  # 1 día

# Database
spring.datasource.url=jdbc:mysql://localhost:3307/venuecatalog
spring.datasource.username=user
spring.datasource.password=password

# Flyway
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
```

## 📝 Licencia

Proyecto desarrollado como parte del curso de Spring Boot - Arquitectura Hexagonal.

## 👥 Autor

Desarrollado con ❤️ siguiendo las mejores prácticas de Spring Boot y arquitectura hexagonal.

# Docker Guide for VenueCatalog REST

## What is Docker?

Docker is a platform that allows you to package, distribute, and run applications in isolated containers. Think of containers as lightweight, portable boxes that contain everything your application needs to run: code, runtime, libraries, and dependencies.

### Key Concepts

1. **Image**: A blueprint/template for creating containers. Like a recipe.
2. **Container**: A running instance of an image. Like a dish made from the recipe.
3. **Dockerfile**: Instructions to build an image.
4. **Docker Compose**: Tool to define and run multi-container applications.

## How This Dockerfile Works

### Multi-Stage Build

Our `Dockerfile` uses a **multi-stage build** approach with two stages:

#### Stage 1: Build Stage
```dockerfile
FROM maven:3.9.6-eclipse-temurin-17 AS build
```
- Uses a Maven image with Java 17
- Named "build" so we can reference it later
- Downloads dependencies and compiles the application
- Creates the JAR file

**Why separate?** The build stage includes Maven and source code (heavy), but we don't need these in the final image.

#### Stage 2: Runtime Stage
```dockerfile
FROM eclipse-temurin:17-jre-alpine
```
- Uses a lightweight Alpine Linux image with only Java Runtime (JRE)
- Copies only the compiled JAR from the build stage
- Much smaller final image (~200MB vs ~800MB)

### Layer Caching

```dockerfile
COPY pom.xml .
RUN mvn dependency:go-offline -B
```
Docker caches each instruction as a layer. By copying `pom.xml` first and downloading dependencies separately, Docker can reuse this layer if dependencies haven't changed, making rebuilds faster.

### Health Check

```dockerfile
HEALTHCHECK --interval=30s --timeout=3s ...
```
Docker periodically checks if the application is healthy by calling the health endpoint. If it fails, the container is marked as unhealthy.

## Commands

### Build the Image

```bash
# Build manually
docker build -t venuecatalog-rest:latest .

# Or use docker-compose (recommended)
docker-compose build
```

**What happens:**
1. Docker reads the Dockerfile
2. Downloads base images (Maven, JRE)
3. Copies your code
4. Runs Maven to build the JAR
5. Creates final lightweight image with just the JAR

### Run the Container

```bash
# Using docker-compose (recommended)
docker-compose up

# Or run manually
docker run -p 8080:8080 --name venuecatalog venuecatalog-rest:latest
```

**What happens:**
1. Docker creates a container from the image
2. Maps port 8080 from container to your host
3. Starts the Spring Boot application
4. Application is accessible at http://localhost:8080

### Useful Commands

```bash
# Start in background (detached mode)
docker-compose up -d

# View logs
docker-compose logs -f

# Stop containers
docker-compose down

# Rebuild and restart
docker-compose up --build

# List running containers
docker ps

# Execute commands inside container
docker exec -it venuecatalog-rest sh

# View container resource usage
docker stats venuecatalog-rest
```

## How Docker Compose Works

`docker-compose.yml` defines your application's services:

```yaml
services:
  venuecatalog-app:
    build: .              # Build from Dockerfile in current directory
    ports:
      - "8080:8080"       # Map host:container ports
    environment:          # Set environment variables
      - JAVA_OPTS=-Xmx512m
    networks:             # Connect to custom network
      - venuecatalog-network
```

**Benefits:**
- Single command to start everything
- Easy to add databases, Redis, etc.
- Consistent environment across team
- Easy to scale services

## Environment Variables

You can customize the application without rebuilding:

```bash
# Set JVM memory
docker run -e JAVA_OPTS="-Xmx1g" -p 8080:8080 venuecatalog-rest

# Or in docker-compose.yml
environment:
  - JAVA_OPTS=-Xmx1g -Xms512m
  - SPRING_PROFILES_ACTIVE=production
```

## Best Practices Applied

1. **Multi-stage build**: Smaller final image
2. **Layer caching**: Faster rebuilds
3. **Non-root user**: Better security (can be added)
4. **Health checks**: Automatic monitoring
5. **.dockerignore**: Faster builds, smaller context
6. **Alpine base**: Minimal attack surface

## Troubleshooting

### Container won't start
```bash
# Check logs
docker-compose logs

# Check if port is already in use
lsof -i :8080
```

### Out of memory
```bash
# Increase JVM memory
docker-compose up -e JAVA_OPTS="-Xmx1g"
```

### Slow builds
```bash
# Clean Docker cache
docker system prune -a

# Use BuildKit for faster builds
DOCKER_BUILDKIT=1 docker build -t venuecatalog-rest .
```

## Next Steps

To use a real database instead of H2:

1. Add PostgreSQL service to `docker-compose.yml`:
```yaml
services:
  postgres:
    image: postgres:15-alpine
    environment:
      POSTGRES_DB: venuecatalog
      POSTGRES_USER: admin
      POSTGRES_PASSWORD: secret
    volumes:
      - postgres-data:/var/lib/postgresql/data

volumes:
  postgres-data:
```

2. Update `application.properties` to use PostgreSQL
3. Connect services via Docker network

## Resources

- [Docker Documentation](https://docs.docker.com/)
- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [Spring Boot with Docker](https://spring.io/guides/topicals/spring-boot-docker/)

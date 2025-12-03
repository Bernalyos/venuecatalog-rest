# Multi-stage build for optimized image size

# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies (cached layer)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application (skip tests for faster builds)
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:17-jre-alpine

# Set working directory
WORKDIR /app

# Copy the JAR from build stage
COPY --from=build /app/target/venuecatalog-rest-*.jar app.jar

# Railway uses PORT environment variable, default to 8080
ENV PORT=8080
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Expose the port (Railway will override this)
EXPOSE ${PORT}

# Health check (Railway compatible)
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:${PORT}/actuator/health || exit 1

# Run the application with dynamic port
# Railway will inject PORT environment variable
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=${PORT} -jar app.jar"]
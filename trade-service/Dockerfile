# Stage 1: Build the application
FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /app

# Copy the project files and build the JAR
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime image
FROM openjdk:17-jdk-slim
WORKDIR /app

# Set environment variables for database credentials
ENV DB_USERNAME=root
ENV DB_PASSWORD=yourpassword

# Copy the built JAR file
COPY --from=builder /app/target/trade-service-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 8081

# Start the application
ENTRYPOINT ["java", "-jar", "app.jar"]
